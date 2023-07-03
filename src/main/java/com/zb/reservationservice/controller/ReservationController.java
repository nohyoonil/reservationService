package com.zb.reservationservice.controller;

import com.zb.reservationservice.form.ReservationForm;
import com.zb.reservationservice.model.ReservationResponse;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.service.ReservationService;
import com.zb.reservationservice.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/reservation")
@RestController
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestHeader("JWT-TOKEN") String token,
                                               @RequestBody ReservationForm reservationForm) {

        TokenInfo tokenInfo = JWTUtils.getUserTokenInfo(token);
        reservationService.createReservation(tokenInfo, reservationForm);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 점주 예약 승인 - 점주
     */
    @PostMapping("/{reservationId}/approve")
    public ResponseEntity<?> approveReservation(@RequestHeader("JWT-TOKEN") String token,
                                                @PathVariable long reservationId) {

        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        reservationService.approve(tokenInfo, reservationId);

        return ResponseEntity.ok().build();
    }

    /**
     * 점주 예약 거절 - 점주
     */
    @PostMapping("/{reservationId}/reject")
    public ResponseEntity<?> rejectReservation(@RequestHeader("JWT-TOKEN") String token,
                                               @PathVariable long reservationId) {

        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        reservationService.reject(tokenInfo, reservationId);

        return ResponseEntity.ok().build();
    }

    /**
     * 해당 매점 예약 타임 테이블 조회 - 점주
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getReservationTable(@RequestHeader("JWT-TOKEN") String token,
                                                 @PathVariable long storeId) {

        TokenInfo tokenInfo = JWTUtils.getOwnerTokenInfo(token);
        List<ReservationResponse> reservationTable = reservationService.getReservationTable(tokenInfo, storeId);

        return ResponseEntity.ok(reservationTable);
    }


    /**
     * 회원 도착 -> 예약 성공 - 회원
     */
    @PostMapping("/{reservationId}/success")
    public ResponseEntity<?> succeedReservation(@RequestHeader("JWT-TOKEN") String token,
                                                @PathVariable long reservationId) {

        TokenInfo tokenInfo = JWTUtils.getUserTokenInfo(token);
        reservationService.succeed(tokenInfo, reservationId);

        return ResponseEntity.ok().build();
    }

    /**
     * 리뷰 가능한 예약들 가져오기 - 회원
     */
    @GetMapping("/reviewable")
    public ResponseEntity<?> getReviewableReservations(@RequestHeader("JWT-TOKEN") String token) {

        TokenInfo tokenInfo = JWTUtils.getUserTokenInfo(token);

        List<ReservationResponse> reservations = reservationService.getReviewableReservations(tokenInfo);

        return ResponseEntity.ok(reservations);
    }
}
