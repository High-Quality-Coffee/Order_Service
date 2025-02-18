package com.teamsparta14.order_service.product.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.product.service.ProductService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestParam(value = "sort", defaultValue = "LATEST") SortBy sortBy
    ){
        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponseDto> products = productService.getProducts(storeId, pageable, sortBy);

        return ResponseEntity.ok().body(ApiResponse.success(products));
    }

    //상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductDetails(@PathVariable("productId") UUID productId) {

        ProductResponseDto responseDto = productService.getProductDetails(productId);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 등록
    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        ProductResponseDto responseDto = productService.addProduct(requestDto.getStore_id(), requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 수정
    @PutMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.updateProduct(requestDto.getStore_id(), productId, requestDto);

        return ResponseEntity.ok().body(ApiResponse.success(responseDto));
    }

    //상품 삭제
    @PostMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> deleteProduct(@PathVariable("productId") UUID productId) {

        return ResponseEntity.ok().body(ApiResponse.success(productService.deleteProduct(productId)));
    }
}
