package com.teamsparta14.order_service.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreRequestDto {
    private String storeName;
    private String address;
    private String phone;
    private List<String> categories;
}
