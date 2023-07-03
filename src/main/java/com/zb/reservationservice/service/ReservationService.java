package com.zb.reservationservice.service;

import com.zb.reservationservice.entity.Reservation;
import com.zb.reservationservice.entity.Store;
import com.zb.reservationservice.entity.User;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.ReservationForm;
import com.zb.reservationservice.model.ReservationResponse;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.repository.ReservationRepository;
import com.zb.reservationservice.repository.StoreRepository;
import com.zb.reservationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;


    public void createReservation(TokenInfo tokenInfo, ReservationForm reservationForm) {
        if (reservationForm.getReservationTime().isBefore(LocalDateTime.now().plusMinutes(20))) {
            throw new CustomException(ErrorCode.UNAVAILABLE_RESERVATION_TIME);
        }

        Store store = storeRepository.findById(reservationForm.getStoreId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_EXISTS));

        User user = userRepository.findById(tokenInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        Reservation reservation = Reservation.builder()
                .user(user)
                .store(store)
                .reservationTime(reservationForm.getReservationTime())
                .createdAt(LocalDateTime.now())
                .build();

        reservationRepository.save(reservation);
    }

    /**
     * 예약 승인
     */
    public void approve(TokenInfo tokenInfo, long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_EXISTS));

        validationCheck(tokenInfo, reservation);

        reservation.setApproved(true);
        
        reservationRepository.save(reservation);
    }

    /**
     * 예약 거절
     */
    public void reject(TokenInfo tokenInfo, long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_EXISTS));

        validationCheck(tokenInfo, reservation);

        reservation.setRejected(true);

        reservationRepository.save(reservation);
    }

    /**
     * 예약시간, 권한, 승인, 거절 유효성 체크
     */
    private void validationCheck(TokenInfo tokenInfo, Reservation reservation) {
        if (reservation.getStore().getOwner().getId() != tokenInfo.getId()) {
            throw new CustomException(ErrorCode.HAS_NO_AUTHORIZATION);
        }

        if (reservation.getReservationTime().isBefore(LocalDateTime.now().plusMinutes(20))) {
            throw new CustomException(ErrorCode.UNAVAILABLE_RESERVATION_TIME);
        }

        isNotApprovedAndRejected(reservation);
    }

    private void isNotApprovedAndRejected(Reservation reservation) {
        if (reservation.isApproved()) {
            throw new CustomException(ErrorCode.ALREADY_APPROVED);
        }

        if (reservation.isRejected()) {
            throw new CustomException(ErrorCode.ALREADY_REJECTED);
        }
    }

    /**
     * 유저 도착 - 예약 성공
     */
    public void succeed(TokenInfo tokenInfo, long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_EXISTS));

        if (reservation.getUser().getId() != tokenInfo.getId()) {
            throw new CustomException(ErrorCode.HAS_NO_AUTHORIZATION);
        }

        isNotApprovedAndRejected(reservation);

        if (LocalDateTime.now().isAfter(reservation.getReservationTime().minusMinutes(10))) {
            throw new CustomException(ErrorCode.VISITED_TIME_OVER);
        }

        reservation.setConcluded(true);

        reservationRepository.save(reservation);
    }

    /**
     *  리뷰 가능한 예약들 가져오기
     */
    public List<ReservationResponse> getReviewableReservations(TokenInfo tokenInfo) {
        User user = userRepository.findById(tokenInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        ArrayList<ReservationResponse> responses = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByUser(user);

        for (Reservation reservation : reservations) {
            if (reservation.isConcluded() && !reservation.isReviewed()) {
                responses.add(ReservationResponse.of(reservation));
            }
        }

        return responses;
    }

    /**
     * 해당 매점 예약 타임 테이블 조회
     */
    public List<ReservationResponse> getReservationTable(TokenInfo tokenInfo, long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_EXISTS));

        if (store.getOwner().getId() != tokenInfo.getId()) {
            throw new CustomException(ErrorCode.HAS_NO_AUTHORIZATION);
        }

        List<ReservationResponse> responses = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByStore(store);

        for (Reservation reservation : reservations) {
            if (reservation.isApproved() && !reservation.isConcluded()
                    && reservation.getReservationTime().isAfter(LocalDateTime.now().plusMinutes(10))) {
                responses.add(ReservationResponse.of(reservation));
            }
        }

        return responses;
    }
}
