package com.zb.reservationservice.model;

import com.zb.reservationservice.entity.Reservation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationResponse {
    private long id;
    private StoreResponse store;
    private LocalDateTime reservationTime;
    private LocalDateTime createdAt;

    public static ReservationResponse of(Reservation reservation) {
        ReservationResponse reservationResponse = new ReservationResponse();

        reservationResponse.id = reservation.getId();
        reservationResponse.store = StoreResponse.of(reservation.getStore());
        reservationResponse.reservationTime = reservation.getReservationTime();
        reservationResponse.createdAt = reservation.getCreatedAt();

        return reservationResponse;
    }
}
