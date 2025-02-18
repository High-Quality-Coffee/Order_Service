package com.teamsparta14.order_service.store.service;

import com.teamsparta14.order_service.store.dto.StoreRequestDto;
import com.teamsparta14.order_service.store.dto.StoreResponseDto;
import com.teamsparta14.order_service.store.dto.StoreUpdateRequestDto;
import com.teamsparta14.order_service.store.entity.Category;
import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreCategory;
import com.teamsparta14.order_service.store.repository.StoreCategoryRepository;
import com.teamsparta14.order_service.store.repository.CategoryRepository;
import com.teamsparta14.order_service.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
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
        List<StoreCategory> storeCategories = storeCategoryRepository.findByStoreId(storeId);
        return storeCategories.stream()
                .map(storeCategory -> storeCategory.getCategory().getCategoryName())
                .collect(Collectors.toList());
    }

    // [조회] 삭제되지 않은 가게만
    public List<StoreResponseDto> getAllStores() {
        return storeRepository.findByIsDeletedFalse()
                .stream()
                .map(store -> new StoreResponseDto(store, getCategoryNames(store.getId())))
                .collect(Collectors.toList());
    }

    // [등록] 가게 (owner만)
    @Transactional
    public StoreResponseDto createStore(StoreRequestDto dto, String createdBy) {
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

    // [수정] 가게 정보
    @Transactional
    public StoreResponseDto updateStore(UUID storeId, StoreUpdateRequestDto requestDto, String modifiedBy) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("해당 가게를 찾을 수 없습니다."));

        // OWNER 본인 가게인지 검증
        if (!store.getCreatedBy().equals(modifiedBy)) {
            throw new RuntimeException("해당 가게를 수정할 권한이 없습니다.");
        }

        // 가게 정보 업데이트
        store.setStoreName(requestDto.getStoreName());
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());

        storeRepository.save(store);

        // 기존 카테고리 삭제 후 재등록
        if (requestDto.getCategories() != null && !requestDto.getCategories().isEmpty()) {
            storeCategoryRepository.deleteByStoreId(storeId);
            saveStoreCategories(store, requestDto.getCategories());
        }

        return new StoreResponseDto(store, getCategoryNames(storeId));
    }

    //[삭제] 가게
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

    //카테고리 저장
    private void saveStoreCategories(Store store, List<String> categoryNames) {
        List<Category> categories = categoryRepository.findByCategoryNameIn(categoryNames);
        List<StoreCategory> storeCategories = categories.stream()
                .map(category -> new StoreCategory(store, category)) // 복합 키 기반 저장
                .collect(Collectors.toList());
        storeCategoryRepository.saveAll(storeCategories);
    }
}
