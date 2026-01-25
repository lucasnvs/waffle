package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.common.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.common.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.common.exception.ServiceUnavailableException;
import com.lucasnvs.waffle.common.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.common.exception.InvalidTicketNumberException;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import com.lucasnvs.waffle.ticket.dto.SoldTicketsResponse;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseMessage;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketPurchaseProducer producer;
    private final RaffleRepository raffleRepository;
    private final TicketRepository ticketRepository;

    public TicketService(TicketPurchaseProducer producer, RaffleRepository raffleRepository, TicketRepository ticketRepository) {
        this.producer = producer;
        this.raffleRepository = raffleRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public SoldTicketsResponse getSoldTickets(Long raffleId) {
        raffleRepository.findById(raffleId)
                .orElseThrow(() -> new RaffleNotFoundException(raffleId));

        List<Integer> numbers = ticketRepository.findNumbersByRaffleIdAndStatus(raffleId, TicketStatus.PURCHASED);
        return new SoldTicketsResponse(raffleId, numbers.size(), numbers);
    }

    public void requestPurchase(Long raffleId, PurchaseTicketRequest request, String userId) {
        RaffleEntity raffle = raffleRepository.findById(raffleId)
                .orElseThrow(() -> new RaffleNotFoundException(raffleId));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new RaffleNotOpenException(raffleId);
        }

        validateAllTickets(raffleId, raffle, request.numbers());

        for (Integer ticketNumber : request.numbers()) {
            sendMessage(raffleId, ticketNumber, userId);
        }
    }

    /**
     * Validates all ticket numbers before any are sent to the queue.
     * This ensures atomic behavior - either all tickets are valid or none are processed.
     *
     * @param raffleId the raffle ID
     * @param raffle the raffle entity
     * @param ticketNumbers list of ticket numbers to validate
     * @throws InvalidTicketNumberException if any ticket number is out of range
     * @throws TicketAlreadySoldException if any ticket is already sold
     */
    private void validateAllTickets(Long raffleId, RaffleEntity raffle, List<Integer> ticketNumbers) {
        for (Integer ticketNumber : ticketNumbers) {
            if (ticketNumber <= 0 || ticketNumber > raffle.getTotalTickets()) {
                throw new InvalidTicketNumberException(raffleId, ticketNumber, raffle.getTotalTickets());
            }

            if (ticketRepository.existsByRaffleIdAndNumber(raffleId, ticketNumber)) {
                throw new TicketAlreadySoldException(raffleId, ticketNumber);
            }
        }
    }

    @CircuitBreaker(
            name = "ticketProducer",
            fallbackMethod = "fallback"
    )
    private void sendMessage(Long raffleId, Integer ticketNumber, String userId) {
        producer.send(new TicketPurchaseMessage(
                raffleId,
                ticketNumber,
                userId
        ));
    }

    private void fallback(Long raffleId, Integer ticketNumber, String userId, Throwable ex) {
        throw new ServiceUnavailableException("Ticket service temporarily unavailable", ex);
    }
}

