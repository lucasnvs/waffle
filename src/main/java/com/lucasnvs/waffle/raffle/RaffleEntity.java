package com.lucasnvs.waffle.raffle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "raffles")
@Getter
@Setter
public class RaffleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int totalTickets;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;
}
