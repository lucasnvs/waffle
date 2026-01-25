package com.lucasnvs.waffle.raffle;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "raffles")
@Getter
@Setter
public class RaffleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerId; // Firebase UID do criador da rifa

    private String title;

    private int totalTickets;

    private double ticketPrice;

    private String description;

    private boolean hasDrawDate;

    private LocalDateTime drawDate;

    private LocalTime drawTime;

    private String coverImage;

    private String contactPhoneNumber;

    @Column(name = "is_public")
    private boolean public_;

    private boolean showWinnerPublicly;

    @ElementCollection(targetClass = RafflePaymentMethod.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "raffle_payment_methods",
            joinColumns = @JoinColumn(name = "raffle_id")
    )
    @Column(name = "payment_method", nullable = false)
    private Set<RafflePaymentMethod> paymentMethods = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private RaffleDrawMethod drawMethod;

    @Enumerated(EnumType.STRING)
    private RaffleStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, unique = true)
    private String slug;
}
