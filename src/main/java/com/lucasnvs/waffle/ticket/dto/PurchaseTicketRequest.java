package com.lucasnvs.waffle.ticket.dto;

public record PurchaseTicketRequest(
        Integer number,
        String userId
) {}
