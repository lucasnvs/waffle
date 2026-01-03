package com.lucasnvs.waffle.ticket.dto;

import com.lucasnvs.waffle.ticket.TicketStatus;

public record TicketResponse(
        Long ticketId,
        Long raffleId,
        Integer number,
        TicketStatus status
) {}
