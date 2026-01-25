package com.lucasnvs.waffle.raffle.dto;

import com.lucasnvs.waffle.raffle.RaffleDrawMethod;
import com.lucasnvs.waffle.raffle.RafflePaymentMethod;
import com.lucasnvs.waffle.raffle.RaffleStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public record RaffleResponse(
        Long id,
        String slug,
        String title,
        int totalTickets,
        double ticketPrice,
        String description,
        boolean hasDrawDate,
        LocalDateTime drawDate,
        LocalTime drawTime,
        String coverImage,
        String contactPhoneNumber,
        boolean isPublic,
        boolean showWinnerPublicly,
        Set<RafflePaymentMethod> paymentMethods,
        RaffleDrawMethod drawMethod,
        RaffleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
