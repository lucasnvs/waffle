package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.ticket.dto.PurchaseTicketRequest;
import com.lucasnvs.waffle.ticket.dto.TicketResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/raffles/{raffleId}/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> purchase(
            @PathVariable Long raffleId,
            @RequestBody @Valid PurchaseTicketRequest request
    ) {
        TicketResponse response = ticketService.purchase(raffleId, request);
        return ResponseEntity.ok(response);
    }
}
