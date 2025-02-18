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
    private UUID productId;
    private String productName;
    private int productPrice;
    private int productQuantity;

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .storeId(product.getStoreId())
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productQuantity(product.getProductQuantity())
                .build();
    }
}