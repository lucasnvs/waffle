package com.lucasnvs.waffle.raffle.dto;

import com.lucasnvs.waffle.raffle.RaffleDrawMethod;
import com.lucasnvs.waffle.raffle.RafflePaymentMethod;
import com.lucasnvs.waffle.raffle.RaffleStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

public record UpdateRaffleRequest(
        String title,
        Integer totalTickets,
        Double ticketPrice,
        String description,
        Boolean hasDrawDate,
        LocalDateTime drawDate,
        LocalTime drawTime,
        String coverImage,
        String contactPhoneNumber,
        Boolean isPublic,
        Boolean showWinnerPublicly,
        Set<RafflePaymentMethod> paymentMethods,
        RaffleDrawMethod drawMethod,
        RaffleStatus status
) {}

