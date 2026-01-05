package com.lucasnvs.waffle.ticket;

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

@RestController
@RequestMapping("/raffles/{raffleId}/tickets")
@Tag(name = "Tickets", description = "API para compra de tickets de rifas")
public class TicketController {

    private final TicketService ticketService;
    private final IdempotencyService idempotencyService;

    public TicketController(TicketService ticketService, IdempotencyService idempotencyService) {
        this.ticketService = ticketService;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping
    @Operation(summary = "Comprar ticket",
               description = "Solicita a compra de um ticket para uma rifa. O processamento é assíncrono.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Solicitação de compra aceita e em processamento"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou X-User-Id não fornecido"),
            @ApiResponse(responseCode = "404", description = "Rifa não encontrada"),
            @ApiResponse(responseCode = "409", description = "Ticket já vendido ou solicitação duplicada"),
            @ApiResponse(responseCode = "429", description = "Limite de requisições excedido"),
            @ApiResponse(responseCode = "503", description = "Serviço temporariamente indisponível")
    })
    public ResponseEntity<?> purchase(
            @Parameter(description = "ID do usuário", required = true)
            @RequestHeader("X-User-Id") String userId,

            @Parameter(description = "ID da rifa")
            @PathVariable Long raffleId,

            @RequestBody @Valid PurchaseTicketRequest request
    ) {

        boolean locked = idempotencyService.tryLock(raffleId, request.number());

        if(!locked) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        ticketService.requestPurchase(raffleId, request);
        return ResponseEntity.accepted().body(
                Map.of(
                        "message", "Your ticket purchase is being processed.",
                        "raffleId", raffleId,
                        "number", request.number()
                )
        );
    }
}
