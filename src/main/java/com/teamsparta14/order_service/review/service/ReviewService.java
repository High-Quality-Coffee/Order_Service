package com.teamsparta14.order_service.review.service;

import com.teamsparta14.order_service.global.response.ProductClientResponse;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.review.dto.RatingDto;
import com.teamsparta14.order_service.review.dto.ReviewRequestDto;
import com.teamsparta14.order_service.review.dto.ReviewResponseDto;
import com.teamsparta14.order_service.review.entity.Review;
import com.teamsparta14.order_service.review.repository.ReviewRepository;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final JWTUtil jwtUtil;

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
    public ReviewResponseDto createReview(ReviewRequestDto requestDto, String token) {

        String userName = jwtUtil.getUsername(token);
        
        //작성한 리뷰가 있는지 확인
        if (reviewRepository.existsByStoreIdAndUserName(requestDto.getStoreId(), userName)) {
            throw new IllegalArgumentException("이미 작성한 리뷰가 있습니다.");
        }

        Review review = reviewRepository.save(Review.from(requestDto, userName));
        
        //가게로 별점 보내기
        RatingDto ratingDto = new RatingDto(review.getStar());

        //storeId와 별점을 전달할 URL
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8080")
                .path("/api/stores/" + requestDto.getStoreId() + "/rating")
                .encode()
                .build()
                .toUri();



        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("star",requestDto.getStar().getValue());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("access", token);

        HttpEntity<Map<String, Integer>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        System.out.println(response.getBody());

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new IllegalArgumentException("can not update store rating");
        }


        return ReviewResponseDto.of(review);
    }

    //리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(UUID reviewId, ReviewRequestDto requestDto, String token) {

        String userName = jwtUtil.getUsername(token);
        Review review = checkWriterAndFind(reviewId, userName);

        review.update(requestDto, userName);

        return ReviewResponseDto.of(review);
    }

    //리뷰 삭제
    @Transactional
    public ReviewResponseDto deleteReview(UUID reviewId, String token) {

        String userName = jwtUtil.getUsername(token);
        Review review = checkWriterAndFind(reviewId, userName);

        review.delete();
        review.setDeleted(LocalDateTime.now(), userName);

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
