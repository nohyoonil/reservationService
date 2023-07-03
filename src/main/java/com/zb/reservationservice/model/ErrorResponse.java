package com.zb.reservationservice.model;

import com.zb.reservationservice.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
