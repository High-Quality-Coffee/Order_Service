package com.teamsparta14.order_service.review.dto;

import com.teamsparta14.order_service.review.entity.Review;
import com.teamsparta14.order_service.review.entity.Stars;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequestDto {

    private UUID storeId;
    private UUID orderId;
    private String review;
    private Stars star;

    public static ReviewRequestDto from(Review review){

        return ReviewRequestDto.builder()
                .storeId(review.getStoreId())
                .orderId(review.getOrderId())
                .review(review.getReview())
                .star(review.getStar())
                .build();
    }
}
