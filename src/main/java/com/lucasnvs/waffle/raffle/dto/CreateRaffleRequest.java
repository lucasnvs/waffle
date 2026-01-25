package com.lucasnvs.waffle.raffle.dto;

import com.lucasnvs.waffle.raffle.RaffleDrawMethod;
import com.lucasnvs.waffle.raffle.RafflePaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Schema(description = "Dados para criação de uma nova rifa")
public record CreateRaffleRequest(
        @Schema(description = "Título da rifa", example = "iPhone 15 Pro Max")
        @NotBlank(message = "Title is required")
        String title,

        @Schema(description = "Número total de tickets", example = "100")
        @Positive(message = "Total tickets must be positive")
        int totalTickets,

        @Schema(description = "Preço de cada ticket", example = "50.0")
        @Positive(message = "Ticket price must be positive")
        double ticketPrice,

        @Schema(description = "Descrição detalhada da rifa", example = "Sorteio de um iPhone 15 Pro Max 256GB")
        String description,

        @Schema(description = "Se a rifa possui data de sorteio definida", example = "true")
        boolean hasDrawDate,

        @Schema(description = "Data e hora do sorteio", example = "2026-02-01T20:00:00")
        LocalDateTime drawDate,

        @Schema(description = "Hora do sorteio", example = "20:00:00")
        LocalTime drawTime,

        @Schema(description = "URL da imagem de capa", example = "https://example.com/iphone.jpg")
        String coverImage,

        @Schema(description = "Telefone de contato", example = "+5511999999999")
        String contactPhoneNumber,

        @Schema(description = "Se a rifa é pública", example = "true")
        boolean isPublic,

        @Schema(description = "Se o vencedor será mostrado publicamente", example = "true")
        boolean showWinnerPublicly,

        @Schema(description = "Métodos de pagamento aceitos", example = "[\"PIX\", \"CREDIT_CARD\"]")
        @NotNull(message = "Payment methods are required")
        Set<RafflePaymentMethod> paymentMethods,

        @Schema(description = "Método de sorteio", example = "ONLINE_SORTER")
        @NotNull(message = "Draw method is required")
        RaffleDrawMethod drawMethod
) {}
