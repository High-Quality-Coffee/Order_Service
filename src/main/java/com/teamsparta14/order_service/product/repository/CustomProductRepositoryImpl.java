package com.teamsparta14.order_service.product.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teamsparta14.order_service.product.dto.ProductSearchDto;
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

    @Override
    public UUID findStoreIdByProductId(UUID productId) {
        return queryFactory
                .select(product.storeId)
                .from(product)
                .where(product.id.eq(productId))
                .fetchOne();
    }

    @Override
    public boolean existsByStoreIdAndProductId(UUID storeId, UUID productId) {
        Boolean isDeleted = queryFactory
                .select(product.isDeleted)
                .from(product)
                .where(product.storeId.eq(storeId).and(product.id.eq(productId)))
                .fetchOne();

        return isDeleted != null && !isDeleted;
    }

    //공통 쿼리 메서드
    private List<Product> getProductQuery(UUID storeId, String keyword, Pageable pageable, SortBy sortBy, ProductStatus status) {

        int pageSize = validatePageSize(pageable.getPageSize());

        return queryFactory
                .selectFrom(product)
                .where(
                        product.storeId.eq(storeId)
                                .and(getTitleLike(product.productName, keyword))
                                .and(product.isDeleted.eq(false))
                                .and(getStatusCondition(status))
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
    /*private BooleanExpression getTitleLike(String keyword) {
        return StringUtils.hasText(keyword) ? product.productName.contains(keyword) : null;
    }*/

    //상태 조건
    private BooleanExpression getStatusCondition(ProductStatus status) {
        return product.status.eq(status != null ? status : ProductStatus.ON_SALE);
    }

    //정렬 조건
    private OrderSpecifier<?> getOrderSpecifier(SortBy sortBy) {
        return sortBy == SortBy.MODIFIED ?
                product.modifiedAt.desc() :
                product.createdAt.desc();
    }

    //한국어/영어 혼합 검색
    private BooleanExpression getTitleLike(StringPath path, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        String trimmedKeyword = keyword.trim();

        //영어 문자가 포함되어 있는지 확인 (정규식 사용)
        boolean containsEnglish = trimmedKeyword.matches(".*[a-zA-Z]+.*");

        if (containsEnglish) {
            //영어가 포함된 경우 대소문자 구분 없이 검색
            return path.toLowerCase().contains(trimmedKeyword.toLowerCase());
        } else {
            //한국어만 있는 경우
            return path.like("%" + trimmedKeyword + "%");
        }
    }
}