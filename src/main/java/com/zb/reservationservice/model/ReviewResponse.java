package com.zb.reservationservice.model;

import com.zb.reservationservice.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private long id;
    private String content;
    private LocalDateTime createdAt;

    private StoreResponse store;

    public static ReviewResponse of(Review review) {
        ReviewResponse reviewResponse = new ReviewResponse();

        reviewResponse.id = review.getId();
        reviewResponse.content = review.getContent();
        reviewResponse.createdAt = review.getCreatedAt();
        reviewResponse.store = StoreResponse.of(review.getStore());

        return reviewResponse;
    }
}
