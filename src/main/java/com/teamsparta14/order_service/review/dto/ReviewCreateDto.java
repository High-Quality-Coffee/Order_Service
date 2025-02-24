package com.teamsparta14.order_service.review.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewCreateDto {

    private UUID storeId;
    private List<ReviewRequestDto> reviewRequestList;
}
