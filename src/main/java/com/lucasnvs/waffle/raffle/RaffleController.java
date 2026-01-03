package com.lucasnvs.waffle.raffle;

import com.lucasnvs.waffle.raffle.dto.CreateRaffleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/raffles")
public class RaffleController {

    private final RaffleService raffleService;

    public RaffleController(RaffleService raffleService) {
        this.raffleService = raffleService;
    }

    @PostMapping
    public ResponseEntity<RaffleEntity> create(@RequestBody CreateRaffleRequest request) {
        RaffleEntity createdRaffle = raffleService.createRaffle(request.title(), request.totalTickets());
        return ResponseEntity.ok(createdRaffle);
    }
}
