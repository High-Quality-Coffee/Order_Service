package com.teamsparta14.order_service.store.dto;

import com.teamsparta14.order_service.store.entity.Store;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class StoreResponseDto {
    private UUID id;
    private String storeName;
    private String address;
    private String phone;
    private List<String> categories;

    // 기본 생성자
    public StoreResponseDto(Store store) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.categories = store.getCategories() != null ? store.getCategories() : Collections.emptyList();
    }

    // categories 리스트를 직접 전달받는 생성자 추가
    public StoreResponseDto(Store store, List<String> categories) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.categories = categories != null ? categories : Collections.emptyList();
    }
}
