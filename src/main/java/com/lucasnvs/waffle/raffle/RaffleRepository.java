package com.lucasnvs.waffle.raffle;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RaffleRepository extends JpaRepository<RaffleEntity, Long> {
}
