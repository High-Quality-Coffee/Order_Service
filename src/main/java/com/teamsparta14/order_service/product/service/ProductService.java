package com.teamsparta14.order_service.product.service;

import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    //상품 전체 조회
    public List<ProductResponseDto> getProducts(Long storeId) {

        List<Product> productList = productRepository.findAllByStoreId(storeId);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            if(product.isDeleted()) continue;
            responseDtoList.add(ProductResponseDto.of(product));
        }

        return responseDtoList;
    }

    //상품 상세 조회
    public ProductResponseDto getProductDetails(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

        if(product.isDeleted()) throw new EntityNotFoundException("삭제된 상품입니다.");

        return ProductResponseDto.of(product);
    }

    //상품 등록
    public ProductResponseDto addProduct(Long storeId, ProductRequestDto requestDto) {

        Product product = productRepository.save(new Product(requestDto, storeId));

        return ProductResponseDto.of(product);
    }
    
    //상품 수정
    @Transactional
    public ProductResponseDto updateProduct(Long storeId, Long productId, ProductRequestDto requestDto) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.upadte(product.getId(), requestDto);

        return ProductResponseDto.of(product);
    }

    //상품 삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.delete();
        product.setDeleted(LocalDateTime.now(), "User");
    }
}
