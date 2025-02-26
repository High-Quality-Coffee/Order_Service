package com.teamsparta14.order_service.store.dto;

import lombok.Data;
import java.util.List;

@Data
public class StoreUpdateRequestDto {
    private String storeName;
    private String address;
    private String phone;
    private List<String> categories;
}