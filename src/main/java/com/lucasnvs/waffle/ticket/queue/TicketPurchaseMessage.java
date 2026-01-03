package com.lucasnvs.waffle.ticket.queue;

public record TicketPurchaseMessage(
        Long raffleId,
        Integer number,
        String userId
) {}