package com.lucasnvs.waffle.raffle;

import com.lucasnvs.waffle.raffle.dto.CreateRaffleRequest;
import com.lucasnvs.waffle.raffle.dto.RaffleResponse;
import com.lucasnvs.waffle.raffle.dto.UpdateRaffleRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/raffles")
public class RaffleController {

    private final RaffleService raffleService;

    public RaffleController(RaffleService raffleService) {
        this.raffleService = raffleService;
    }

    @PostMapping
    public ResponseEntity<RaffleResponse> create(@Valid @RequestBody CreateRaffleRequest request) {
        RaffleResponse createdRaffle = raffleService.createRaffle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRaffle);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RaffleResponse> getRaffleById(@PathVariable Long id) {
        RaffleResponse raffle = raffleService.getRaffleById(id);
        return ResponseEntity.ok(raffle);
    }

    @GetMapping
    public ResponseEntity<List<RaffleResponse>> getAllRaffles() {
        List<RaffleResponse> raffles = raffleService.getAllRaffles();
        return ResponseEntity.ok(raffles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RaffleResponse> updateRaffle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRaffleRequest request) {
        RaffleResponse updatedRaffle = raffleService.updateRaffle(id, request);
        return ResponseEntity.ok(updatedRaffle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRaffle(@PathVariable Long id) {
        raffleService.deleteRaffle(id);
        return ResponseEntity.noContent().build();
    }
}
