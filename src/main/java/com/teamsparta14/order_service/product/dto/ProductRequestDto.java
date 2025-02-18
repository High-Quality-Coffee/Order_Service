package com.teamsparta14.order_service.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductRequestDto {

    private UUID storeId;
    private String productName;
    private Long productPrice;
    private Long productQuantity;
}