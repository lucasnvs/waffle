package com.lucasnvs.waffle.ticket.queue;

import com.lucasnvs.waffle.config.RabbitConfig;
import com.lucasnvs.waffle.raffle.RaffleEntity;
import com.lucasnvs.waffle.raffle.RaffleRepository;
import com.lucasnvs.waffle.ticket.TicketEntity;
import com.lucasnvs.waffle.ticket.TicketRepository;
import com.lucasnvs.waffle.ticket.TicketStatus;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
public class TicketPurchaseConsumer {
    private static final Logger log = LoggerFactory.getLogger(TicketPurchaseConsumer.class);

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
        try {
            RaffleEntity raffle = raffleRepository
                    .findById(message.raffleId())
                    .orElseThrow();

            TicketEntity ticket = new TicketEntity();
            ticket.setNumber(message.number());
            ticket.setUserId(message.userId());
            ticket.setStatus(TicketStatus.PURCHASED);
            ticket.setRaffle(raffle);

            ticketRepository.save(ticket);

            log.info("Ticket comprado com sucesso - raffle_id: {}, number: {}, user_id: {}",
                    message.raffleId(), message.number(), message.userId());
        } catch (DataIntegrityViolationException e) {
            log.warn("Ticket duplicado ignorado - raffle_id: {}, number: {}, user_id: {}",
                    message.raffleId(), message.number(), message.userId());
        }
    }
}
