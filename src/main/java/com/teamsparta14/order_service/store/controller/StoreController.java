package com.teamsparta14.order_service.store.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.review.dto.RatingDto;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.SortBy;
import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreStatus;
import com.teamsparta14.order_service.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;


    // [GET] 전체 가게 조회
    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<Page<StoreResponseDto>>> getAllStores(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) StoreStatus status,
            @RequestParam(value = "sort", defaultValue = "CREATED_AT") SortBy sortBy
    ) {
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, sortBy.getSort());
        Page<StoreResponseDto> stores = storeService.getAllStores(pageable, status);

        return ResponseEntity.ok(ApiResponse.success(stores));
    }

    // [GET] 특정 가게 조회
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> getStore(@PathVariable(name = "storeId") UUID storeId,@RequestHeader("access") String token) {
        Store store = storeService.getStoreById(storeId , token);
        StoreResponseDto storeResponseDto =  new StoreResponseDto(store);
        return ResponseEntity.ok(ApiResponse.success(storeResponseDto));
    }

    // [POST] 가게 등록
    @PostMapping("/stores")
    public ResponseEntity<ApiResponse<StoreResponseDto>> createStore(
            @RequestBody StoreRequestDto dto,
            @RequestHeader(name = "access") String token
    ) {
        //when then given

        return ResponseEntity.ok(ApiResponse.success(storeService.createStore(dto, token)));
    }

    // [PUT] 가게 정보 수정
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> updateStore(
            @PathVariable("storeId") UUID storeId,
            @RequestBody StoreUpdateRequestDto requestDto,
            @RequestHeader(name  = "access") String token
    ) {


        return ResponseEntity.ok(ApiResponse.success(
                storeService.updateStore(storeId, requestDto, token)
        ));
    }

    // [DELETE] 가게 삭제
    @DeleteMapping("stores/{storeId}")
    public ResponseEntity<ApiResponse<String>> deleteStore(
            @PathVariable("storeId") UUID storeId,
            @RequestHeader(name  = "access") String token
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.deleteStore(storeId, token)));
    }

    // [조회] 모든 카테고리
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(storeService.getAllCategories()));
    }

    // [조회] 특정 카테고리
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById( @PathVariable(name="categoryId") UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getCategoryById(categoryId)));
    }

    // [등록] 카테고리
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @RequestBody CategoryRequestDto requestDto,
            @RequestHeader(name = "access") String token
    ) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createCategory(requestDto, token)));
    }

    // [수정] 카테고리
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(
            @PathVariable(name="categoryId") UUID categoryId,
            @RequestBody CategoryRequestDto requestDto,
            @RequestHeader(name = "access") String token
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.updateCategory(categoryId, requestDto, token)));
    }

    // [삭제] 카테고리
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable(name="categoryId") UUID categoryId,
            @RequestHeader(name = "access") String token
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.deleteCategory(categoryId, token)));
    }

    // [조회] 모든 지역
    @GetMapping("/regions")
    public ResponseEntity<ApiResponse<List<RegionResponseDto>>> getAllRegions() {
        return ResponseEntity.ok(ApiResponse.success(storeService.getAllRegions()));
    }

    // [조회] 특정 지역
    @GetMapping("/regions/{regionId}")
    public ResponseEntity<ApiResponse<RegionResponseDto>> getRegionById(@PathVariable(name = "regionId") UUID regionId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getRegionById(regionId)));
    }

    // [등록] 지역
    @PostMapping("/regions")
    public ResponseEntity<ApiResponse<RegionResponseDto>> createRegion(
            @RequestBody RegionRequestDto requestDto,
            @RequestHeader(name = "access") String token
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.createRegion(requestDto, token)));
    }

    // [수정] 지역
    @PutMapping("/regions/{regionId}")
    public ResponseEntity<ApiResponse<RegionResponseDto>> updateRegion(
            @PathVariable(name = "regionId") UUID regionId,
            @RequestBody RegionRequestDto requestDto,
            @RequestHeader(name = "access") String token
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.updateRegion(regionId, requestDto, token)));
    }

    // [삭제] 지역
    @DeleteMapping("/regions/{regionId}")
    public ResponseEntity<ApiResponse<String>> deleteRegion(
            @PathVariable(name = "regionId") UUID regionId,
            @RequestHeader(name = "access") String token
    ) {
        return ResponseEntity.ok(ApiResponse.success(storeService.deleteRegion(regionId, token)));
    }


    // [수정] 리뷰 점수
    @PostMapping("/stores/{storeId}/rating")
    public ResponseEntity<String> addStoreRating(
            @PathVariable(name = "storeId") UUID storeId,
            @RequestBody RatingDto ratingDto,
            @RequestHeader("access") String token
    ) {
        storeService.addStoreRating(storeId, ratingDto.getStar(), token);
        return ResponseEntity.ok("업체 평점이 업데이트되었습니다.");
    }
}