package com.teamsparta14.order_service.store.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.SortBy;
import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreStatus;
import com.teamsparta14.order_service.store.service.StoreService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    // [GET] 전체 가게 조회
    @GetMapping
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
    @GetMapping("/{storeId}")
    public ResponseEntity<Store> getStore(@PathVariable UUID storeId) {
        Store store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(store);
    }

    // [POST] 가게 등록
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto>> createStore(
            @RequestBody StoreRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {

        return ResponseEntity.ok(ApiResponse.success(storeService.createStore(dto)));
    }

    // [PUT] 가게 정보 수정
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER', 'ROLE_ADMIN')")
    @PutMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> updateStore(
            @PathVariable UUID storeId,
            @RequestBody StoreUpdateRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Store store = storeService.getStoreById(storeId);
        String currentUsername = customUserDetails.getUsername();

        boolean isAdmin = customUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if (!isAdmin && !store.getCreatedBy().equals(currentUsername)) {
            throw new RuntimeException("본인이 소유한 가게만 수정할 수 있습니다.");
        }

        return ResponseEntity.ok(ApiResponse.success(
                storeService.updateStore(storeId, requestDto)
        ));
    }

    // [DELETE] 가게 삭제
    @PreAuthorize("hasAnyAuthority('ROLE_OWNER', 'ROLE_ADMIN')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResponse<String>> deleteStore(
            @PathVariable("storeId") UUID storeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String currentUsername = customUserDetails.getUsername();

        boolean isAdmin = customUserDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        Store store = storeService.getStoreById(storeId);

        if (!isAdmin && !store.getCreatedBy().equals(currentUsername)) {
            throw new RuntimeException("본인이 소유한 가게만 삭제할 수 있습니다.");
        }

        String deleteMessage = storeService.deleteStore(storeId, currentUsername);
        return ResponseEntity.ok(ApiResponse.success(deleteMessage));
    }

    // [조회] 모든 카테고리
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getAllCategories() {
        return ResponseEntity.ok(ApiResponse.success(storeService.getAllCategories()));
    }

    // [조회] 특정 카테고리
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getCategoryById(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.getCategoryById(categoryId)));
    }

    // [등록] 카테고리
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(@RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createCategory(requestDto)));
    }

    // [수정] 카테고리
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(storeService.updateCategory(categoryId, requestDto)));
    }

    // [삭제] 카테고리
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(ApiResponse.success(storeService.deleteCategory(categoryId)));
    }

    // [등록] 지역
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @PostMapping("/regions")
    public ResponseEntity<ApiResponse<RegionResponseDto>> createRegion(@RequestBody RegionRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success(storeService.createRegion(requestDto)));
    }

    // [수정] 리뷰 점수
//    @PostMapping("/{storeId}/rating")
//    public ResponseEntity<String> updateStoreRating(@PathVariable UUID storeId, @RequestBody RatingUpdateDto ratingUpdateDto) {
//        storeService.updateStoreRating(storeId, ratingUpdateDto.getTotalReviewCount(), ratingUpdateDto.getAverageRating());
//        return ResponseEntity.ok("업체 평점이 업데이트되었습니다.");
//    }
}
