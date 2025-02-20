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
    private String createdBy;

    // 기본 생성자
    public StoreResponseDto(Store store, List<String> categories) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.createdBy = store.getCreatedBy();
        this.categories = categories != null ? categories : Collections.emptyList(); //빈값이 아니면 카테고리
    }
}
