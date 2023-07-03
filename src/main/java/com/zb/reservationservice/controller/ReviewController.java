package com.zb.reservationservice.controller;

import com.zb.reservationservice.exception.CustomException;
import com.zb.reservationservice.exception.ErrorCode;
import com.zb.reservationservice.form.ReviewForm;
import com.zb.reservationservice.model.ReviewResponse;
import com.zb.reservationservice.model.TokenInfo;
import com.zb.reservationservice.service.ReviewService;
import com.zb.reservationservice.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/review")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 해당 가게 리뷰 조회 - all
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<?> getReviewsByStore(@PathVariable long storeId) {

        List<ReviewResponse> reviews = reviewService.getReviewsByStoreId(storeId);

        return ResponseEntity.ok(reviews);
    }

    /**
     * 회원 리뷰 작성 - 회원
     */
    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<?> createReview(@RequestHeader("JWT-TOKEN") String token,
                                          @RequestBody @Valid ReviewForm reviewForm, Errors errors,
                                          @PathVariable long reservationId) {

        if (errors.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_DATA_INPUT);
        }

        TokenInfo tokenInfo = JWTUtils.getUserTokenInfo(token);

        reviewService.createReview(tokenInfo, reviewForm, reservationId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
