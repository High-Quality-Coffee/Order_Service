package com.teamsparta14.order_service.review.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.review.dto.ReviewRequestDto;
import com.teamsparta14.order_service.review.dto.ReviewResponseDto;
import com.teamsparta14.order_service.review.service.ReviewService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    //리뷰 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponseDto>>> getReviews(
            @RequestParam("store_id") UUID storeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "LATEST") SortBy sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<ReviewResponseDto> products = reviewService.getReviews(storeId, pageable, sortBy);

        return ResponseEntity.ok().body(ApiResponse.success(products));
    }

    //리뷰 상세 조회
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> getReviewDetails(@PathVariable("reviewId") UUID reviewId) {

        ReviewResponseDto responseDto = reviewService.getReviewDetails(reviewId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //리뷰 등록
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDto>> createReview(
            @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String user = customUserDetails.getUsername();

        ReviewResponseDto responseDto = reviewService.createReview(requestDto, user);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> updateReview(
            @PathVariable("reviewId") UUID reviewId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String user = customUserDetails.getUsername();

        ReviewResponseDto responseDto = reviewService.updateReview(reviewId, requestDto, user);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //리뷰 삭제
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDto>> deleteReview(
            @PathVariable("reviewId") UUID reviewId, @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        String user = customUserDetails.getUsername();

        return ResponseEntity.ok().body(ApiResponse.success(reviewService.deleteReview(reviewId, user)));
    }
}
