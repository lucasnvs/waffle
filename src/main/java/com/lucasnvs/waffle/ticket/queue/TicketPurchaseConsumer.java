package com.lucasnvs.waffle.ticket.queue;

import com.lucasnvs.waffle.config.RabbitConfig;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.ticket.TicketEntity;
import com.lucasnvs.waffle.ticket.TicketRepository;
import com.lucasnvs.waffle.ticket.TicketStatus;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketPurchaseConsumer {
    private final TicketRepository ticketRepository;
    private final RaffleRepository raffleRepository;

    public TicketPurchaseConsumer(
            TicketRepository ticketRepository,
            RaffleRepository raffleRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.raffleRepository = raffleRepository;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    @Transactional
    public void consume(TicketPurchaseMessage message) {
        RaffleEntity raffle = raffleRepository
                .findById(message.raffleId())
                .orElseThrow();

        TicketEntity ticket = new TicketEntity();
        ticket.setNumber(message.number());
        ticket.setUserId(message.userId());
        ticket.setStatus(TicketStatus.PURCHASED);
        ticket.setRaffle(raffle);

        ticketRepository.save(ticket);
    }
}
