package com.lucasnvs.waffle.raffle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaffleRepository extends JpaRepository<RaffleEntity, Long> {
    List<RaffleEntity> findByOwnerId(String ownerId);
    boolean existsBySlug(String slug);
}
