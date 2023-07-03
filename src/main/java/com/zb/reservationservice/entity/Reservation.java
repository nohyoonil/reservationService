package com.zb.reservationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Store store;

    private LocalDateTime reservationTime;
    private LocalDateTime createdAt;

    private boolean isApproved; //점주 승인
    private boolean isRejected; //점주 거절
    private boolean isConcluded; // 회원 도착 - 예약 성공
    private boolean isReviewed; // 리뷰 작성 여부

}
