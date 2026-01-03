package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.exception.ServiceUnavailableException;
import com.lucasnvs.waffle.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseMessage;
import com.lucasnvs.waffle.ticket.queue.TicketPurchaseProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

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

    @CircuitBreaker(
            name = "ticketProducer",
            fallbackMethod = "fallback"
    )
    public void requestPurchase(Long raffleId, PurchaseTicketRequest request) {
        RaffleEntity raffle = raffleRepository.findById(raffleId).orElseThrow(() -> new RaffleNotFoundException(raffleId));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new RaffleNotOpenException(raffleId);
        }

        if (ticketRepository.existsByRaffleIdAndNumber(raffleId, request.number())) {
            throw new TicketAlreadySoldException(raffleId, request.number());
        }

        producer.send(new TicketPurchaseMessage(
                raffleId,
                request.number(),
                request.userId()
        ));
    }

    private void fallback(Long raffleId, PurchaseTicketRequest request, Throwable ex) {
        throw new ServiceUnavailableException("Ticket service temporarily unavailable", ex);
    }
}

