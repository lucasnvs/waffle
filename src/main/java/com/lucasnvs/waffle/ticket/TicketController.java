package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/raffles/{raffleId}/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<?> purchase(
            @PathVariable Long raffleId,
            @RequestBody @Valid PurchaseTicketRequest request
    ) {
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
