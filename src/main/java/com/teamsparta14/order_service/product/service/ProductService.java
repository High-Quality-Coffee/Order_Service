package com.teamsparta14.order_service.product.service;

import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //상품 전체 조회
    public List<ProductResponseDto> getProducts(UUID storeId, Pageable pageable, SortBy sortBy, ProductStatus status) {
        List<Product> productList = productRepository.findAllByStoreId(storeId, pageable, sortBy, status);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(ProductResponseDto.of(product));
        }

        return responseDtoList;
    }

    //상품 상세 조회
    public ProductResponseDto getProductDetails(UUID productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        return ProductResponseDto.of(product);
    }

    //상품 등록
    @Transactional
    public ProductResponseDto addProduct(UUID storeId, ProductRequestDto requestDto) {

        AIDescription aiDescription = new AIDescription();

        String description = aiDescription.getDescription(requestDto.getProductName());

        Product product = productRepository.save(new Product(requestDto,storeId,description));

        return ProductResponseDto.of(product);
    }

    //상품 수정
    @Transactional
    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto requestDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.update(requestDto);

        return ProductResponseDto.of(product);
    }

    //상품 삭제
    @Transactional
    public ProductResponseDto deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.delete();
        product.setDeleted(LocalDateTime.now(), "User");

        return ProductResponseDto.of(product);
    }

    //상품 수량 업데이트
    @Transactional
    public ProductResponseDto updateProductQuantity(UUID productId, ProductRequestDto requestDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.updateOrderCount(requestDto.getProductQuantity());
        productRepository.save(product);

        return ProductResponseDto.of(product);
    }

    //상품 상태 변경
    @Transactional
    public ProductResponseDto updateProductStatus(UUID productId, ProductStatus status) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.updateStatus(status);

        return ProductResponseDto.of(product);
    }
    
    //상품 검색
    public List<ProductResponseDto> searchByTitle(UUID storeId, String keyword, Pageable pageable, SortBy sortBy, ProductStatus status) {

        if(keyword == null) keyword = "";

        List<Product> productList = productRepository.findByTitleContain(storeId, keyword,pageable, sortBy, status);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(ProductResponseDto.of(product));
        }

        return responseDtoList;
    }
}