package com.teamsparta14.order_service.store.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.product.entity.ProductStatus;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.SortBy;
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
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // [GET] 가게 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponseDto>>> getAllStores(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) StoreStatus status,
            @RequestParam(value = "sort", defaultValue = "CREATED_AT") SortBy sortBy
            ) {
        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(page, size, sortBy.getSort());
        List<StoreResponseDto> stores = storeService.getAllStores(pageable, status);

        return ResponseEntity.ok(ApiResponse.success(stores));
    }

    // [POST] 가게 등록
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponseDto>> createStore(
            @RequestBody StoreRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String createdBy = customUserDetails.getUsername();
        return ResponseEntity.ok(ApiResponse.success(storeService.createStore(dto, createdBy)));
    }

    // [PUT] 가게 정보 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<ApiResponse<StoreResponseDto>> updateStore(
            @PathVariable UUID storeId,
            @RequestBody StoreUpdateRequestDto requestDto
    ) {
        CustomUserDetails userDetails = getAuthenticatedUser();
        String role = extractRole(userDetails);

        if (!"OWNER".equals(role) && !"ADMIN".equals(role)) {
            throw new AccessDeniedException("가게 수정은 OWNER 또는 ADMIN만 가능합니다.");
        }

        return ResponseEntity.ok(ApiResponse.success(
                storeService.updateStore(storeId, requestDto, userDetails.getUsername(), role)
        ));
    }

    // [DELETE] 가게 삭제
    @DeleteMapping("/{storeId}")

    public ResponseEntity<ApiResponse<Void>> deleteStore(
            @PathVariable("storeId") UUID storeId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
// JWT에서 어케 가져오지
//        String role = customUserDetails.getUser().getRole().name();
//
//        if (!"ADMIN".equals(role)) {
//            throw new RuntimeException("가게 삭제는 ADMIN만 가능합니다.");
//        }
//
//        storeService.deleteStore(storeId);

        storeService.deleteStore(storeId, customUserDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // [POST] 카테고리 등록
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> createCategory(
            @RequestBody CategoryRequestDto requestDto
    ) {
        CustomUserDetails userDetails = getAuthenticatedUser();
        String role = extractRole(userDetails);

        if (!"ADMIN".equals(role)) {
            throw new RuntimeException("카테고리 등록은 관리자만 가능합니다.");
        }
        return ResponseEntity.ok(ApiResponse.success(storeService.createCategory(requestDto)));
    }

    // [GET] 모든 카테고리 조회 ??
//    @GetMapping("/categories")
//    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
//        return ResponseEntity.ok(storeService.getAllCategories());
//    }

    // 사용자 정보 가져오기 → 확인 필요
    private CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new RuntimeException("인증된 사용자가 없습니다.");
        }
        return (CustomUserDetails) authentication.getPrincipal();
    }

    private String extractRole(CustomUserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            return authority.getAuthority(); // 첫 번째 권한만 반환
        }
        throw new RuntimeException("사용자의 역할을 찾을 수 없습니다.");
    }

}
