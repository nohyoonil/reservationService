package com.zb.reservationservice.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReservationForm {

    private long storeId;
    private LocalDateTime reservationTime;
}
