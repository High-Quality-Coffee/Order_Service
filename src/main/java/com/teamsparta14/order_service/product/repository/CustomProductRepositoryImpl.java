package com.teamsparta14.order_service.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.teamsparta14.order_service.product.entity.QProduct.product;

@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final JPAQueryFactory queryFactory;

    private static final int DEFAULT_SIZE = 10;
    private static final List<Integer> ALLOWED_PAGE_SIZES = Arrays.asList(10, 30, 50);

    @Override
    public List<Product> findAllByStoreId(UUID storeId, Pageable pageable, SortBy sortBy, ProductStatus status) {
        //페이지 사이즈 검증 및 조정
        int pageSize = ALLOWED_PAGE_SIZES.contains(pageable.getPageSize()) ? pageable.getPageSize() : DEFAULT_SIZE;

        JPAQuery<Product> query = queryFactory
                .selectFrom(product)
                .where(
                        product.storeId.eq(storeId),
                        product.isDeleted.eq(false),
                        statusEq(status)
                );

        //정렬 옵션
        switch (sortBy) {
            case MODIFIED:
                query.orderBy(product.modifiedAt.desc());
                break;
            case LATEST:
            default:
                query.orderBy(product.createdAt.desc());
                break;
        }

        return query
                .offset(pageable.getOffset())
                .limit(pageSize)
                .fetch();
    }

    //상품 상태 검증
    private BooleanExpression statusEq(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }

    @Override
    public Optional<Product> findByProductId(UUID productId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(product)
                        .where(
                                product.id.eq(productId),
                                product.isDeleted.eq(false)
                        )
                        .fetchOne()
        );
    }
}
