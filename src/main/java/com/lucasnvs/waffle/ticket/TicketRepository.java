package com.lucasnvs.waffle.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    boolean existsByRaffleIdAndNumber(Long raffleId, Integer number);
}

