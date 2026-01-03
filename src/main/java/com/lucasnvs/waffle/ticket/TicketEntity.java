package com.lucasnvs.waffle.ticket;

import com.lucasnvs.waffle.raffle.RaffleEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "tickets",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"raffle_id", "number"})
        }
)
@Getter
@Setter
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private String userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "raffle_id")
    private RaffleEntity raffle;
}
