package com.lucasnvs.waffle.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    boolean existsByRaffleIdAndNumber(Long raffleId, Integer number);
}

