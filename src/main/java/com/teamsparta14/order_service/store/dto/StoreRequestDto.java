package com.teamsparta14.order_service.store.dto;

import lombok.Getter;
import lombok.Setter;
import com.teamsparta14.order_service.store.entity.StoreStatus;
import java.util.List;

@Getter
@Setter
public class StoreRequestDto {
    private String storeName;
    private String address;
    private String phone;
    private List<String> categories;
    private String regionName; // 지역 추가
    private StoreStatus status = StoreStatus.OPEN; // 기본값 설정
}
