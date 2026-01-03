package com.lucasnvs.waffle.exception;

public class TicketAlreadySoldException extends RuntimeException {
    public TicketAlreadySoldException(Long raffleId, Integer ticketNumber) {
        super("Ticket number " + ticketNumber + " is already sold in raffle " + raffleId);
    }
}

