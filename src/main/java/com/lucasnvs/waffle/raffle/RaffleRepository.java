package com.lucasnvs.waffle.raffle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RaffleRepository extends JpaRepository<RaffleEntity, Long> {
}
