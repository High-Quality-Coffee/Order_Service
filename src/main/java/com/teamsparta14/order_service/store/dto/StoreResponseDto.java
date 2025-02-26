package com.teamsparta14.order_service.store.dto;

import com.teamsparta14.order_service.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {
    private UUID id;
    private String storeName;
    private String address;
    private String phone;
    private List<String> categories;
    private String regionName;
    private double averageRating;

    // 기본 생성자
    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.averageRating = store.getAverageRating();

        this.categories = store.getStoreCategories() != null
                ? store.getStoreCategories().stream()
                .map(storeCategory -> storeCategory.getCategoryId().getCategoryName())
                .collect(Collectors.toList())
                : Collections.emptyList();

        this.regionName = store.getRegion() != null ? store.getRegion().getRegionName() : "지역 정보 없음";
    }
}
