package com.teamsparta14.order_service.product.dto;

import com.teamsparta14.order_service.product.entity.Product;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {

    private UUID storeId;
    private String productName;
    private int productPrice;

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .storeId(product.getStoreId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .build();
    }
}
