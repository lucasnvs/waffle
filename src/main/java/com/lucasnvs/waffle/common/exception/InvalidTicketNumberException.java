package com.lucasnvs.waffle.common.exception;

public class InvalidTicketNumberException extends RuntimeException {
    private final Long raffleId;
    private final Integer ticketNumber;
    private final Integer totalTickets;

    public InvalidTicketNumberException(Long raffleId, Integer ticketNumber, Integer totalTickets) {
        super(String.format(
                "Invalid ticket number %d for raffle %d. Ticket number must be between 1 and %d",
                ticketNumber, raffleId, totalTickets
        ));
        this.raffleId = raffleId;
        this.ticketNumber = ticketNumber;
        this.totalTickets = totalTickets;
    }

    public Long getRaffleId() {
        return raffleId;
    }

    public Integer getTicketNumber() {
        return ticketNumber;
    }

    public Integer getTotalTickets() {
        return totalTickets;
    }
}

