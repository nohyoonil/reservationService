package com.zb.reservationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Reservation reservation;
    @ManyToOne
    private User user;
    @ManyToOne
    private Store store;

    private int score;
    private String content;
    private LocalDateTime createdAt;
}
