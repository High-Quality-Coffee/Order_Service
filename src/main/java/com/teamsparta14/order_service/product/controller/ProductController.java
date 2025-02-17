package com.teamsparta14.order_service.product.controller;

import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.service.ProductService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<ProductResponseDto>> getProducts(@RequestParam("store_id") UUID storeId){

        List<ProductResponseDto> products = productService.getProducts(storeId);

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    //상품 상세 조회
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProductDetails(@PathVariable("productId") UUID productId) {

        ProductResponseDto responseDto = productService.getProductDetails(productId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //상품 등록
    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.addProduct(requestDto.getStore_id(), requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //상품 수정
    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductRequestDto requestDto) {

        ProductResponseDto responseDto = productService.updateProduct(requestDto.getStore_id(), productId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    //상품 삭제
    @PostMapping("/products/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") UUID productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 완료");
    }
}
