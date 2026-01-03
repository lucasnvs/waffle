package com.lucasnvs.waffle.exception;

public class RaffleNotFoundException extends RuntimeException {
    public RaffleNotFoundException(Long raffleId) {
        super("Raffle not found with id: " + raffleId);
    }
}

