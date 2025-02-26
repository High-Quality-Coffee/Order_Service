package com.teamsparta14.order_service.review.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.review.entity.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.teamsparta14.order_service.review.entity.QReview.review1;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final JPAQueryFactory queryFactory;

    private static final int DEFAULT_SIZE = 10;
    private static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);


    @Override
    public List<Review> findAllByStoreId(UUID storeId, Pageable pageable, SortBy sortBy) {
        //페이지 사이즈 검증 및 조정
        int pageSize = ALLOWED_PAGE_SIZES.contains(pageable.getPageSize()) ? pageable.getPageSize() : DEFAULT_SIZE;

        JPAQuery<Review> query = queryFactory
                .selectFrom(review1)
                .where(
                        review1.storeId.eq(storeId),
                        review1.isDeleted.eq(false)
                );

        //정렬 옵션
        switch (sortBy) {
            case MODIFIED:
                query.orderBy(review1.modifiedAt.desc());
                break;
            case LATEST:
            default:
                query.orderBy(review1.createdAt.desc());
                break;
        }

        return query
                .offset(pageable.getOffset())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public Optional<Review> findByReviewId(UUID reviewId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(review1)
                        .where(
                                review1.id.eq(reviewId),
                                review1.isDeleted.eq(false)
                        )
                        .fetchOne()
        );
    }

    @Override
    public String findWriterByReviewId(UUID reviewId) {

        return queryFactory
                .select(review1.userName)
                .from(review1)
                .where(
                        review1.id.eq(reviewId),
                        review1.isDeleted.eq(false)
                )
                .fetchOne();
    }

    @Override
    public boolean existsByStoreIdAndUserName(UUID storeId, String user) {
        return queryFactory.selectFrom(review1)
                .where(review1.storeId.eq(storeId)
                        .and(review1.userName.eq(user)))
                .fetchCount() > 0;
    }
}
