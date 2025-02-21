package com.teamsparta14.order_service.product.repository;

import com.teamsparta14.order_service.product.dto.ProductSearchDto;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomProductRepository {

    //상품 전체 조회
    List<Product> findAllByStoreId(UUID storeId, Pageable pageable, SortBy sortBy, ProductStatus status);

    //상품 검색
    List<Product> findByTitleContain(UUID storeId, String keyword, Pageable pageable, SortBy sortBy, ProductStatus status);

    //상품 상세 조회
    Optional<Product> findByProductId(UUID productId);

    List<Product> searchProductByIdList(ProductSearchDto requestDto);

    UUID findStoreIdByProductId(UUID productId);
}
