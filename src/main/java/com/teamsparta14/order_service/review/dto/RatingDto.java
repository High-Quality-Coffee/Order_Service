package com.teamsparta14.order_service.review.dto;

import com.teamsparta14.order_service.review.entity.Stars;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RatingDto {

    private int star; //별점

    public RatingDto(Stars star) {
        this.star = star.getValue();
    }
}
