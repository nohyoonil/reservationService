package com.zb.reservationservice.exception;

import com.zb.reservationservice.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> customExceptionHandler(CustomException customException) {
        ErrorCode errorCode = customException.getErrorCode();

        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode, errorCode.getMessage()));
    }
}
