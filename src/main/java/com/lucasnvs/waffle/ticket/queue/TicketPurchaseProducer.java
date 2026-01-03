package com.lucasnvs.waffle.ticket.queue;

import com.lucasnvs.waffle.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketPurchaseProducer {
    private final RabbitTemplate rabbitTemplate;

    public TicketPurchaseProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(TicketPurchaseMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                message
        );
    }
}
