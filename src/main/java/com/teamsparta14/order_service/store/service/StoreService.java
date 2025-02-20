package com.teamsparta14.order_service.store.service;

import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.Category;
import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreCategory;
import com.teamsparta14.order_service.store.entity.StoreStatus;
import com.teamsparta14.order_service.store.repository.StoreCategoryRepository;
import com.teamsparta14.order_service.store.repository.CategoryRepository;
import com.teamsparta14.order_service.store.repository.StoreRepository;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.entity.UserEntity;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final CategoryRepository categoryRepository;

    // [조회] 가게
    public Page<StoreResponseDto> getAllStores(Pageable pageable, StoreStatus status) {
        Page<Store> stores;

        if (status != null) {
            stores = storeRepository.findByStatusAndIsDeletedFalse(status, pageable);
        } else {
            stores = storeRepository.findByIsDeletedFalse(pageable);
        }

        return stores.map(store -> new StoreResponseDto(store, getCategoryNames(store.getId())));
    }

    // [조회] 특정 가게
    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다."));
    }

    // [등록] 가게
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto dto, String createdBy) {

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isDeleted(false)
                .createdBy(createdBy)
                .status(dto.getStatus() != null ? dto.getStatus() : StoreStatus.OPEN)
                .build();

        Store savedStore = storeRepository.save(store);
        saveStoreCategories(savedStore, dto.getCategories());

        return new StoreResponseDto(savedStore, dto.getCategories());
    }

    // [수정] 가게
    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다."));

        store.update(requestDto);
        storeRepository.save(store);

        return new StoreResponseDto(store, requestDto.getCategories());
    }

    // [삭제] 가게
    @Transactional
    public String deleteStore(UUID storeId, String deletedBy) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        store.setDeleted(true);
        store.setDeletedBy(deletedBy);
        storeRepository.save(store);

        return "가게 ID " + storeId + "가 성공적으로 삭제되었습니다.";
    }

    // [조회] 카테고리
    private List<String> getCategoryNames(UUID storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreId(store);

        return storeCategories.stream()
                .map(storeCategory -> storeCategory.getCategoryId() != null ? storeCategory.getCategoryId().getCategoryName() : "Unknown Category")
                .collect(Collectors.toList());
    }

    // [등록] 카테고리 저장
    private void saveStoreCategories(Store store, List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);
        List<StoreCategory> storeCategories = categories.stream()
                .map(category -> new StoreCategory(store, category)) // 필드명 변경에 맞게 수정
                .collect(Collectors.toList());
        storeCategoryRepository.saveAll(storeCategories);
    }

    // [등록] 카테고리
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

}
