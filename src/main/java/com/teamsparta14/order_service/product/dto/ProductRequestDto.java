package com.teamsparta14.order_service.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductRequestDto {

    private UUID store_id;
    private String product_name;
    private int product_price;
}
