package com.teamsparta14.order_service.review.dto;

import com.teamsparta14.order_service.review.entity.Review;
import com.teamsparta14.order_service.review.entity.Stars;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
public class ReviewResponseDto {

    private UUID storeId;
    private UUID reviewId;
    private String userName;
    private String review;
    private Stars star;

    public static ReviewResponseDto of(Review review) {
        return ReviewResponseDto.builder()
                .storeId(review.getStoreId())
                .reviewId(review.getId())
                .userName(review.getUserName())
                .review(review.getReview())
                .star(review.getStar())
                .build();
    }
}
