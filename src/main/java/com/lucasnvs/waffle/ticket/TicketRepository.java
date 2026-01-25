package com.lucasnvs.waffle.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    boolean existsByRaffleIdAndNumber(Long raffleId, Integer number);

    @Query("select t.number from TicketEntity t where t.raffle.id = :raffleId and t.status = :status")
    List<Integer> findNumbersByRaffleIdAndStatus(@Param("raffleId") Long raffleId, @Param("status") TicketStatus status);
}
