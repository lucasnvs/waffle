package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.auth.domain.AuthenticationService;
import com.lucasnvs.waffle.common.idempotency.IdempotencyService;
import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/raffles/{raffleId}/tickets")
@Tag(name = "Tickets", description = "API para compra de tickets de rifas")
public class TicketController {

    private final TicketService ticketService;
    private final IdempotencyService idempotencyService;
    private final AuthenticationService authenticationService;

    public TicketController(TicketService ticketService,
                           IdempotencyService idempotencyService,
                           AuthenticationService authenticationService) {
        this.ticketService = ticketService;
        this.idempotencyService = idempotencyService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @Operation(summary = "Comprar tickets",
               description = "Solicita a compra de um ou mais tickets para uma rifa. O processamento é assíncrono. Requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Solicitação de compra aceita e em processamento"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
            @ApiResponse(responseCode = "404", description = "Rifa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Ticket já vendido ou solicitação duplicada"),
            @ApiResponse(responseCode = "429", description = "Limite de requisições excedido"),
            @ApiResponse(responseCode = "503", description = "Serviço temporariamente indisponível")
    })
    public ResponseEntity<?> purchase(
            @Parameter(description = "ID da rifa")
            @PathVariable Long raffleId,

            @RequestBody @Valid PurchaseTicketRequest request
    ) {
        String userId = authenticationService.getCurrentUserIdOrThrow();

        Integer idempotencyKey = generateIdempotencyKey(raffleId, request, userId);
        boolean locked = idempotencyService.tryLock(raffleId, idempotencyKey);

        if(!locked) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ticketService.requestPurchase(raffleId, request, userId);
        return ResponseEntity.accepted().body(
                Map.of(
                        "message", "Your ticket purchase request is being processed.",
                        "raffleId", raffleId,
                        "ticketsCount", request.numbers().size(),
                        "numbers", request.numbers()
                )
        );
    }

    /**
     * Generates a unique idempotency key based on raffle ID, ticket numbers, and user ID.
     * This ensures that duplicate requests with the same tickets are properly identified.
     *
     * @param raffleId the raffle ID
     * @param request the purchase request containing ticket numbers
     * @param userId the authenticated user ID
     * @return a hash code representing the unique combination
     */
    private Integer generateIdempotencyKey(Long raffleId, PurchaseTicketRequest request, String userId) {
        return Objects.hash(
                raffleId,
                request.numbers(),
                userId
        );
    }
}



