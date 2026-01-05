package com.lucasnvs.waffle.ticket.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record PurchaseTicketRequest(
        @NotEmpty(message = "At least one ticket number must be provided")
        List<Integer> numbers,

        String userId
) {}
