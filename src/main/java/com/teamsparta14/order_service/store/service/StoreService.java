package com.teamsparta14.order_service.store.service;

import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.store.dto.*;
import com.teamsparta14.order_service.store.entity.*;
import com.teamsparta14.order_service.store.repository.*;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.entity.UserEntity;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.teamsparta14.order_service.store.entity.QRegion.region;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final RegionRepository regionRepository;
    private final JWTUtil jwtUtil;

    // [조회] 가게
    public Page<StoreResponseDto> getAllStores(Pageable pageable, StoreStatus status) {
        Page<Store> stores;

        if (status != null) {
            stores = storeRepository.findByStatusAndIsDeletedFalse(status, pageable);
        } else {
            stores = storeRepository.findByIsDeletedFalse(pageable);
        }

        return stores.map(store -> new StoreResponseDto(store));
    }

    // [조회] 특정 가게
    public Store getStoreById(UUID storeId, String token) {

        String userName = jwtUtil.getUsername(token);

        return storeRepository.findById(storeId)
                .filter(store -> !store.isDeleted()) // 삭제된 가게 필터링
                .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다."));
    }

    // [등록] 가게
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto dto, String token) {

        String createdBy = jwtUtil.getUsername(token);
        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole) && !"ROLE_OWNER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        Region region = regionRepository.findByRegionName(dto.getRegionName())
                .orElseThrow(() -> new RuntimeException("해당 지역을 찾을 수 없습니다: " + dto.getRegionName()));


        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isDeleted(false)
                .status(dto.getStatus() != null ? dto.getStatus() : StoreStatus.OPEN)
                .region(region)
                .build();

        store.setCreatedBy(createdBy);
        Store savedStore = storeRepository.save(store);

        if (dto.getCategories() == null || dto.getCategories().isEmpty()) {
            dto.setCategories(List.of());
        }

        List<StoreCategory> storeCategories = saveStoreCategories(savedStore, dto.getCategories());
        savedStore.setStoreCategories(storeCategories);

        storeRepository.save(savedStore);

        return new StoreResponseDto(savedStore);
    }

    // [수정] 가게
    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto, String token) {

        String userName = jwtUtil.getUsername(token);
        String userRole = jwtUtil.getRole(token);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게를 찾을 수 없습니다."));

        // OWNER는 본인 가게만 삭제 가능
        if ("ROLE_OWNER".equals(userRole)) {
            if (store.getCreatedBy() == null || !store.getCreatedBy().equals(userName)) {
                throw new IllegalArgumentException("본인의 가게만 삭제할 수 있습니다.");
            }
        }

        if ("ROLE_USER".equals(userRole) ) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        store.update(requestDto);
        storeRepository.save(store);

        return new StoreResponseDto(store);
    }

    // [삭제] 가게
    @Transactional
    public String deleteStore(UUID storeId, String token) {
        String userName = jwtUtil.getUsername(token);
        String userRole = jwtUtil.getRole(token);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        // OWNER는 본인 가게만 삭제 가능
        if ("ROLE_OWNER".equals(userRole)) {
            if (store.getCreatedBy() == null || !store.getCreatedBy().equals(userName)) {
                throw new IllegalArgumentException("본인의 가게만 삭제할 수 있습니다.");
            }
        }

        if ("ROLE_USER".equals(userRole) ) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        store.setDeleted(true);
        store.setDeletedBy(userName);
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

    // [등록] 가게와 카테고리 저장
    private List<StoreCategory> saveStoreCategories(Store store, List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);

        if (categories.size() != categoryNames.size()) {
            throw new IllegalArgumentException("일부 카테고리가 존재하지 않습니다: " + categoryNames);
        }

        List<StoreCategory> storeCategories = categories.stream()
                .map(category -> new StoreCategory(store, category))
                .collect(Collectors.toList());

        return storeCategoryRepository.saveAll(storeCategories);
    }

    // [조회] 모든 카테고리
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDto::new)
                .collect(Collectors.toList());
    }

    // [조회] 특정 카테고리
    public CategoryResponseDto getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("해당 카테고리를 찾을 수 없습니다."));
        return new CategoryResponseDto(category);
    }

    // [등록] 카테고리
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto dto, String token) {

        boolean exists = categoryRepository.existsByCategoryName(dto.getCategoryName());
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        Category category = Category.builder()
                .categoryName(dto.getCategoryName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponseDto(savedCategory);
    }

    // [수정] 카테고리
    @Transactional
    public CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto dto, String token) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        category.setCategoryName(dto.getCategoryName());
        return new CategoryResponseDto(categoryRepository.save(category));
    }

    // [삭제] 카테고리
    @Transactional
    public String deleteCategory(UUID categoryId, String token) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다.");
        }

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        categoryRepository.deleteById(categoryId);
        return "카테고리 ID " + categoryId + "가 성공적으로 삭제되었습니다.";
    }

    // [조회] 모든 지역
    public List<RegionResponseDto> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponseDto::new)
                .collect(Collectors.toList());
    }

    // [조회] 특정 지역
    public RegionResponseDto getRegionById(UUID regionId) {
        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new RuntimeException("해당 지역을 찾을 수 없습니다."));
        return new RegionResponseDto(region);
    }

    // [등록] 지역
    @Transactional
    public RegionResponseDto createRegion(RegionRequestDto dto, String token) {

        boolean exists = regionRepository.existsByRegionName(dto.getRegionName());
        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 지역입니다.");
        }

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        Region region = Region.builder()
                .regionName(dto.getRegionName())
                .build();

        Region savedRegion = regionRepository.save(region);
        return new RegionResponseDto(savedRegion);
    }

    // [수정] 지역
    @Transactional
    public RegionResponseDto updateRegion(UUID regionId, RegionRequestDto dto, String token) {

        String userRole = jwtUtil.getRole(token);

        Region region = regionRepository.findById(regionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지역을 찾을 수 없습니다."));


        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        region.setRegionName(dto.getRegionName());
        regionRepository.save(region);
        return new RegionResponseDto(region);
    }


    // [삭제] 지역
    @Transactional
    public String deleteRegion(UUID regionId, String token) {

        if (!regionRepository.existsById(regionId)) {
            throw new IllegalArgumentException("해당 지역을 찾을 수 없습니다.");
        }

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_MASTER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        regionRepository.deleteById(regionId);
        return "지역 ID " + regionId + "가 성공적으로 삭제되었습니다.";
    }

    // [수정] 점수
    @Transactional
    public void addStoreRating(UUID storeId, int newStar, String token) {

        String userRole = jwtUtil.getRole(token);

        if (!"ROLE_USER".equals(userRole)) {
            throw new IllegalArgumentException("접근 권한이 없습니다. 현재 역할: " + userRole);
        }

        Store store = getStoreById(storeId, token);

        // 기존 리뷰 개수, 평균 평점
        int totalReviewCount = store.getTotalReviewCount();
        double averageRating = store.getAverageRating();

        // 계산
        int updatedTotalReviewCount = totalReviewCount + 1;
        double updatedAverageRating = ((averageRating * totalReviewCount) + newStar) / updatedTotalReviewCount;

        store.updateRating(updatedTotalReviewCount, updatedAverageRating);
        storeRepository.save(store);
    }

}
