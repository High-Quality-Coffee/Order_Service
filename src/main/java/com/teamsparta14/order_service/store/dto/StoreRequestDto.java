package com.teamsparta14.order_service.store.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String regionName;
    private StoreStatus status = StoreStatus.OPEN;

    @JsonProperty("categoryName")
    private List<String> categories;
}
