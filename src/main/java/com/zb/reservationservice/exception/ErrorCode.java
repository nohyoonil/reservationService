package com.zb.reservationservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 가입된 정보가 있습니다"),
    USER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다"),
    STORE_NOT_EXISTS(HttpStatus.BAD_REQUEST, "매점이 존재하지 않습니다"),
    RESERVATION_NOT_EXISTS(HttpStatus.BAD_REQUEST, "예약이 존재하지 않습니다"),
    ALREADY_APPROVED(HttpStatus.BAD_REQUEST, "이미 승인된 예약입니다"),
    ALREADY_REJECTED(HttpStatus.BAD_REQUEST, "이미 거절된 예약입니다"),
    ALREADY_REVIEWED(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하였습니다"),
    RESERVATION_NOT_SUCCEEDED(HttpStatus.BAD_REQUEST, "성사되지 않은 예약입니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "올바른 비밀번호를 입력해주세요"),
    REQUIRED_LOGIN_AGAIN(HttpStatus.BAD_REQUEST, "다시 로그인해주세요"),
    ALREADY_JOIN_PARTNER(HttpStatus.BAD_REQUEST, "이미 파트너 가입이 완료되었습니다"),
    IS_NOT_PARTNER(HttpStatus.BAD_REQUEST, "파트너 가입이 필요합니다"),
    UNAVAILABLE_RESERVATION_TIME(HttpStatus.BAD_REQUEST, "불가능한 예약시간입니다"),
    VISITED_TIME_OVER(HttpStatus.BAD_REQUEST, "유효한 방문 확인 시간이 지났습니다"),
    HAS_NO_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),
    INVALID_DATA_INPUT(HttpStatus.BAD_REQUEST, "올바른 정보를 입력해주세요");

    private HttpStatus status;
    private String message;

}
