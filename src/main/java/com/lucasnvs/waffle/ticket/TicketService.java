package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.exception.RaffleNotFoundException;
import com.lucasnvs.waffle.exception.RaffleNotOpenException;
import com.lucasnvs.waffle.exception.TicketAlreadySoldException;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.raffle.RaffleStatus;
import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import com.lucasnvs.waffle.ticket.dto.TicketResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final RaffleRepository raffleRepository;

    public TicketService(
            TicketRepository ticketRepository,
            RaffleRepository raffleRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.raffleRepository = raffleRepository;
    }

    @Transactional
    public TicketResponse purchase(
            Long raffleId,
            PurchaseTicketRequest request
    ) {
        RaffleEntity raffle = raffleRepository.findById(raffleId)
                .orElseThrow(() -> new RaffleNotFoundException(raffleId));

        if (raffle.getStatus() != RaffleStatus.OPEN) {
            throw new RaffleNotOpenException(raffleId);
        }

        if (ticketRepository.existsByRaffleIdAndNumber(raffleId, request.number())) {
            throw new TicketAlreadySoldException(raffleId, request.number());
        }

        TicketEntity ticket = new TicketEntity();
        ticket.setNumber(request.number());
        ticket.setUserId(request.userId());
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setRaffle(raffle);

        TicketEntity saved = ticketRepository.save(ticket);

        return new TicketResponse(
                saved.getId(),
                raffleId,
                saved.getNumber(),
                saved.getStatus()
        );
    }
}

