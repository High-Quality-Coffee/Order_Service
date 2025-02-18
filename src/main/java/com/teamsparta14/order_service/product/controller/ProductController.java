package com.teamsparta14.order_service.product.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    //상품 전체 조회
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProducts(
            @RequestParam("store_id") UUID storeId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "LATEST") SortBy sortBy,
            @RequestParam(value = "status", required = false) ProductStatus status
    ){
        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponseDto> products = productService.getProducts(storeId, pageable, sortBy, status);

        return ResponseEntity.ok().body(ApiResponse.success(products));
    }

    //상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductDetails(@PathVariable("productId") UUID productId) {

        ProductResponseDto responseDto = productService.getProductDetails(productId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 검색
    @GetMapping("/products/search")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> searchProducts(
            @RequestParam("store_id") UUID storeId,
            @RequestParam(value ="keyword",required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "LATEST") SortBy sortBy,
            @RequestParam(value = "status", required = false) ProductStatus status
    ){

        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponseDto> products = productService.searchByTitle(storeId, keyword, pageable, sortBy, status);

        return ResponseEntity.ok().body(ApiResponse.success(products));
    }

    //상품 등록
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.addProduct(requestDto.getStoreId(), requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 수정
    @PutMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.updateProduct(productId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 상태 변경
    @PatchMapping("/products/{productId}/status")
    public ResponseEntity<ProductResponseDto> updateProductStatus(
            @PathVariable("productId") UUID productId,
            @RequestParam("status") ProductStatus status
    ) {
        ProductResponseDto responseDto = productService.updateProductStatus(productId, status);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //상품 수량 업데이트
    @PutMapping("/products/{productId}/order")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProductQuantity(@PathVariable("productId") UUID productId, @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.updateProductQuantity(productId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 삭제
    @PostMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> deleteProduct(@PathVariable("productId") UUID productId) {

        return ResponseEntity.ok().body(ApiResponse.success(productService.deleteProduct(productId)));
    }
}