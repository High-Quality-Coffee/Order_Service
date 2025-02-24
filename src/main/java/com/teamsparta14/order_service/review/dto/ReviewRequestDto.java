package com.teamsparta14.order_service.review.dto;

import com.teamsparta14.order_service.review.entity.Stars;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ReviewRequestDto {

    private UUID storeId;
    private UUID orderId;
    private String review;
    private Stars star;
}
