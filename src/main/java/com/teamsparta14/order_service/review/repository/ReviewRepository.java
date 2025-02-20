package com.teamsparta14.order_service.review.repository;

import com.teamsparta14.order_service.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, CustomReviewRepository {
}
