package com.teamsparta14.order_service.store.service;

import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.Category;
import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreCategory;
import com.teamsparta14.order_service.store.repository.StoreCategoryRepository;
import com.teamsparta14.order_service.store.repository.CategoryRepository;
import com.teamsparta14.order_service.store.repository.StoreRepository;
import com.teamsparta14.order_service.user.entity.UserEntity;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final CategoryRepository categoryRepository;

    // [조회] 가게 ID 기반 카테고리 이름 리스트
    private List<String> getCategoryNames(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreId(store);

        return storeCategories.stream()
                .map(storeCategory -> storeCategory.getCategoryId() != null ? storeCategory.getCategoryId().getCategoryName() : "Unknown Category")
                .collect(Collectors.toList());
    }

    // [조회] 삭제되지 않은 가게만
    public List<StoreResponseDto> getAllStores(Pageable pageable) {
        return storeRepository.findByIsDeletedFalse()
                .stream()
                .map(store -> new StoreResponseDto(store, getCategoryNames(store.getId())))
                .collect(Collectors.toList());
    }

    // [등록] 가게 (owner만)
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto dto, String createdBy) {

//        // 1. HTTP 요청에서 JWT 토큰 가져오기
//        String token = extractToken(request);
//        if (token == null) {
//            throw new RuntimeException("JWT 토큰이 필요합니다.");
//        }
//
//        // 2. 현재 로그인한 사용자의 역할(Role) 확인
//        String role = jwtUtil.getRole(token);
//        if (!Role.OWNER.name().equals(role)) {
//            throw new RuntimeException("가게를 등록할 권한이 없습니다.");
//        }
//
//        // 3. 현재 로그인한 사용자의 username 가져오기
//        String username = jwtUtil.getUsername(token);

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isDeleted(false)
                .build();

        Store savedStore = storeRepository.save(store);
        saveStoreCategories(savedStore, dto.getCategories());

        return new StoreResponseDto(savedStore, dto.getCategories());
    }

    // [등록] 카테고리 (관리자만)
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {

        // 카테고리 중복 체크

        // 카테고리 저장
        Category category = Category.builder()
                .categoryName(dto.getCategoryName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDto(savedCategory);
    }

    // [수정] 가게 정보
    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto, String modifiedBy, String role) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        if (!store.getCreatedBy().equals(modifiedBy) && !"ADMIN".equals(role)) {
            throw new RuntimeException("해당 가게를 수정할 권한이 없습니다.");
        }

        store.setStoreName(requestDto.getStoreName());
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());

        storeRepository.save(store);

        if (requestDto.getCategories() != null && !requestDto.getCategories().isEmpty()) {
            storeCategoryRepository.deleteByStore(store);
            saveStoreCategories(store, requestDto.getCategories());
        }

        return new StoreResponseDto(store, getCategoryNames(storeId));
    }

    // [삭제] 가게
    @Transactional
    public void deleteStore(UUID storeId, String deletedBy) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        // OWNER 본인 가게인지 검증
        if (!store.getCreatedBy().equals(deletedBy)) {
            throw new RuntimeException("해당 가게를 삭제할 권한이 없습니다.");
        }

        // 가게 숨김 처리
        store.setDeleted(true);
        storeRepository.save(store);
    }

    // [카테고리 저장]
    private void saveStoreCategories(Store store, List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);
        List<StoreCategory> storeCategories = categories.stream()
                .map(category -> new StoreCategory(store, category)) // 필드명 변경에 맞게 수정
                .collect(Collectors.toList());
        storeCategoryRepository.saveAll(storeCategories);
    }
}
