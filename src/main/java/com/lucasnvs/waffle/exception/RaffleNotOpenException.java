package com.lucasnvs.waffle.exception;

public class RaffleNotOpenException extends RuntimeException {
    public RaffleNotOpenException(Long raffleId) {
        super("Raffle with id " + raffleId + " is not open for purchases");
    }
}

