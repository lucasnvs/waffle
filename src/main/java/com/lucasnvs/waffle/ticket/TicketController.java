package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/raffles/{raffleId}/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final IdempotencyService idempotencyService;

    public TicketController(TicketService ticketService, IdempotencyService idempotencyService) {
        this.ticketService = ticketService;
        this.idempotencyService = idempotencyService;
    }

    @PostMapping
    public ResponseEntity<?> purchase(
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
