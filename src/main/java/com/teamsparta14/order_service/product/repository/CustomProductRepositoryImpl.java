package com.teamsparta14.order_service.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamsparta14.order_service.product.dto.ProductSearchDto;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

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
        return getProductQuery(storeId, null, pageable, sortBy, status);
    }

    @Override
    public List<Product> findByTitleContain(UUID storeId, String keyword, Pageable pageable, SortBy sortBy, ProductStatus status) {
        return getProductQuery(storeId, keyword, pageable, sortBy, status);
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

    @Override
    public List<Product> searchProductByIdList(ProductSearchDto requestDto) {

        return queryFactory
                .selectFrom(product)
                .where(product.id.in(requestDto.getRequestIdList()))
                .fetch();

    }

    //공통 쿼리 메서드
    private List<Product> getProductQuery(UUID storeId, String keyword, Pageable pageable, SortBy sortBy, ProductStatus status) {
        int pageSize = validatePageSize(pageable.getPageSize());

        return queryFactory
                .selectFrom(product)
                .where(
                        product.storeId.eq(storeId),
                        product.isDeleted.eq(false),
                        getTitleLike(keyword),
                        statusEq(status)
                )
                .orderBy(getOrderSpecifier(sortBy))
                .offset(pageable.getOffset())
                .limit(pageSize)
                .fetch();
    }

    //페이지 사이즈 검증
    private int validatePageSize(int requestedSize) {
        return ALLOWED_PAGE_SIZES.contains(requestedSize) ? requestedSize : DEFAULT_SIZE;
    }

    //제목 검색 조건
    private BooleanExpression getTitleLike(String keyword) {
        return StringUtils.hasText(keyword) ? product.productName.contains(keyword) : null;
    }

    //상품 상태 검증
    private BooleanExpression statusEq(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }

    //정렬 조건
    private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
        return sortBy == SortBy.MODIFIED ?
                product.modifiedAt.desc() :
                product.createdAt.desc();
    }
}