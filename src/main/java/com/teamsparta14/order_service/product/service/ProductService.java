package com.teamsparta14.order_service.product.service;

import com.teamsparta14.order_service.global.response.ProductClientResponse;
import com.teamsparta14.order_service.order.repository.StoresClient;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.dto.ProductSearchDto;
import com.teamsparta14.order_service.product.entity.Description;
import com.teamsparta14.order_service.product.entity.Product;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.product.entity.SortBy;
import com.teamsparta14.order_service.product.repository.DescriptionRepository;
import com.teamsparta14.order_service.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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
    private final DescriptionRepository descriptionRepository;
    private final StoresClient storesClient;

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
    public ProductResponseDto addProduct(String token, ProductRequestDto requestDto) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인
        UUID storeId = requestDto.getStoreId();
        /*Object store = storesClient.searchStore(storeId,token);

        if(null == store){
            throw new IllegalArgumentException("store Not found");
        }*/

        //Ai 상품 설명
        AIDescription aiDescription = new AIDescription();

        String aiRequest = requestDto.getProductName() + "란 음식을 50자 이내로 설명해줘";
        String aiResponse = aiDescription.getDescription(aiRequest);

        Description description =  Description.builder()
                .request(aiRequest)
                .response(aiResponse)
                .build();

        descriptionRepository.save(description);

        Product product = productRepository.save(new Product(requestDto,storeId,aiResponse));

        return ProductResponseDto.of(product);
    }

    //상품 수정
    @Transactional
    public ProductResponseDto updateProduct(String token, UUID productId, ProductRequestDto requestDto) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인
        UUID storeId = requestDto.getStoreId();

        //해당 가게와 상품이 맞는 지 체크
        validateStoreAndProduct(token, storeId, productId);

        //Db에서 상품 확인
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.update(requestDto);

        return ProductResponseDto.of(product);
    }

    //상품 삭제
    @Transactional
    public ProductResponseDto deleteProduct(String token, UUID productId) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인
        UUID storeId = productRepository.findStoreIdByProductId(productId);

        //해당 가게와 상품이 맞는 지 체크
        validateStoreAndProduct(token, storeId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.delete();
        product.setDeleted(LocalDateTime.now(), "User");

        return ProductResponseDto.of(product);
    }

    //상품 수량 업데이트
    @Transactional
    public ProductResponseDto updateProductQuantity(String token, UUID productId, ProductRequestDto requestDto) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인
        UUID storeId = productRepository.findStoreIdByProductId(productId);

        //해당 가게와 상품이 맞는 지 체크
        validateStoreAndProduct(token, storeId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("수정할 상품을 찾을 수 없습니다."));

        product.updateOrderCount(requestDto.getProductQuantity());
        productRepository.save(product);

        return ProductResponseDto.of(product);
    }

    //상품 상태 변경
    @Transactional
    public ProductResponseDto updateProductStatus(String token, UUID productId, ProductStatus status) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인
        UUID storeId = productRepository.findStoreIdByProductId(productId);
        
        //해당 가게와 상품이 맞는 지 체크
        validateStoreAndProduct(token, storeId, productId);

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

    public  List<ProductResponseDto> searchProduct(ProductSearchDto requestDto) {

        List<Product> productList = productRepository.searchProductByIdList(requestDto);
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(ProductResponseDto.of(product));
        }

        return responseDtoList;
    }

    //store, product 공통 검증 로직
    private void validateStoreAndProduct(String token, UUID storeId, UUID productId) {
        /*ProductClientResponse store = (ProductClientResponse) storesClient.searchStore(storeId,token);

        if(null == store || store.getData().isEmpty()){
            throw new IllegalArgumentException("해당 가게를 찾을 수 없습니다.");
        }

        //storeId에 등록된 상품인지 체크
        boolean productExists = store.getData().stream()
                .anyMatch(product -> product.getProductId().equals(productId));

        if (!productExists) {
            throw new AccessDeniedException("해당 가게에 속한 상품이 아닙니다.");
        }*/
    }
}