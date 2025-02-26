package com.teamsparta14.order_service.review.repository;

import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.review.entity.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomReviewRepository {

    //리뷰 전체 조회
    List<Review> findAllByStoreId(UUID storeId, Pageable pageable, SortBy sortBy);

    //상품 상세 조회
    Optional<Review> findByReviewId(UUID reviewId);

    //리뷰 작성자 찾기
    String findWriterByReviewId(UUID reviewId);

    boolean existsByStoreIdAndUserName(UUID storeId, String user);
}
