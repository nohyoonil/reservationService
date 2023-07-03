package com.zb.reservationservice.service;

import com.zb.reservationservice.entity.Reservation;
import com.zb.reservationservice.entity.Review;
import com.zb.reservationservice.entity.Store;
import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.ReviewForm;
import com.zb.reservationservice.model.ReviewResponse;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.repository.ReservationRepository;
import com.zb.reservationservice.repository.ReviewRepository;
import com.zb.reservationservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 해당 매점 리뷰 조회
     */
    public List<ReviewResponse> getReviewsByStoreId(long storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_EXISTS));

        ArrayList<ReviewResponse> responses = new ArrayList<>();

        List<Review> reviews = reviewRepository.findByStore(store);

        for (Review review : reviews) {
            responses.add(ReviewResponse.of(review));
        }

        return responses;
    }

    public void createReview(TokenInfo tokenInfo, ReviewForm reviewForm, long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_EXISTS));

        if (reservation.getUser().getId() != tokenInfo.getId()) {
            throw new CustomException(ErrorCode.HAS_NO_AUTHORIZATION);
        }
        if (!reservation.isConcluded()) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_SUCCEEDED);
        }
        if (reservation.isReviewed()) {
            throw new CustomException(ErrorCode.ALREADY_REVIEWED);
        }

        Review review = Review.builder()
                .reservation(reservation)
                .user(reservation.getUser())
                .store(reservation.getStore())
                .score(reviewForm.getScore())
                .content(reviewForm.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }
}
