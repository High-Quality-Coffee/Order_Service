package com.teamsparta14.order_service.store.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RatingUpdateDto {

    @NotNull
    @Min(0)
    private Integer totalReviewCount; // 총 리뷰 개수

    @NotNull
    @Min(0) @Max(5)
    private Double averageRating; // 평균 평점

    public RatingUpdateDto(Integer totalReviewCount, Double averageRating) {
        this.totalReviewCount = totalReviewCount;
        this.averageRating = averageRating;
    }
}
