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
    private Long productPrice;
    private Long productQuantity;
    private String description;

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .storeId(product.getStoreId())
                .productId(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productQuantity(product.getProductQuantity())
                .description(product.getProductDescription())
                .build();
    }

    public void setStoreId(String storeId) {
        this.storeId = UUID.fromString(storeId);
    }

    public void setProductId(String productId) {
        this.productId = UUID.fromString(productId);
    }
}