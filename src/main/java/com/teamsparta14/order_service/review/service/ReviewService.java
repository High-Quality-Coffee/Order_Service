package com.teamsparta14.order_service.review.service;

import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.review.dto.ReviewRequestDto;
import com.teamsparta14.order_service.review.dto.ReviewResponseDto;
import com.teamsparta14.order_service.review.entity.Review;
import com.teamsparta14.order_service.review.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //리뷰 전체 조회
    public List<ReviewResponseDto> getReviews(UUID storeId, Pageable pageable, SortBy sortBy) {
        List<Review> reviewList = reviewRepository.findAllByStoreId(storeId, pageable, sortBy);
        List<ReviewResponseDto> responseDtoList = new ArrayList<>();

        for (Review review : reviewList) {
            responseDtoList.add(ReviewResponseDto.of(review));
        }

        return responseDtoList;
    }

    //리뷰 상세 조회
    public ReviewResponseDto getReviewDetails(UUID reviewId) {

        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        return ReviewResponseDto.of(review);
    }

    //리뷰 등록
    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, String user) {
        
        //작성한 리뷰가 있는지 확인
        if (reviewRepository.existsByStoreIdAndUserName(requestDto.getStoreId(), user)) {
            throw new IllegalArgumentException("이미 작성한 리뷰가 있습니다.");
        }

        Review review = reviewRepository.save(Review.from(requestDto, user));

        return ReviewResponseDto.of(review);
    }

    //리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(UUID reviewId, ReviewRequestDto requestDto, String user) {

        Review review = checkWriterAndFind(reviewId, user);

        review.update(requestDto, user);

        return ReviewResponseDto.of(review);
    }

    //리뷰 삭제
    @Transactional
    public ReviewResponseDto deleteReview(UUID reviewId, String user) {

        Review review = checkWriterAndFind(reviewId, user);

        review.delete();
        review.setDeleted(LocalDateTime.now(), user);

        return ReviewResponseDto.of(review);
    }

    //공통 권한 체크 및 리뷰 찾기
    private Review checkWriterAndFind(UUID reviewId, String user) {
        String writer = reviewRepository.findWriterByReviewId(reviewId);

        if (writer == null || !writer.equals(user)) {
            throw new AccessDeniedException("리뷰를 수정하거나 삭제할 권한이 없습니다.");
        }

        return reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));
    }
}
