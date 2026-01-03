package com.lucasnvs.waffle.raffle;

import org.springframework.stereotype.Service;

@Service
public class RaffleService {

    private final RaffleRepository raffleRepository;

    public RaffleService(RaffleRepository raffleRepository) {
        this.raffleRepository = raffleRepository;
    }

    public RaffleEntity createRaffle(String title, int totalTickets) {
        RaffleEntity raffle = new RaffleEntity();
        raffle.setTitle(title);
        raffle.setTotalTickets(totalTickets);
        raffle.setStatus(RaffleStatus.OPEN);
        return raffleRepository.save(raffle);
    }
}
