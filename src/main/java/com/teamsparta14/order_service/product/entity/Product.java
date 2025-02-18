package com.teamsparta14.order_service.product.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Entity
@Getter
@Table(name="p_product")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID storeId;
    private String productName;
    private Long productPrice;
    private Long productQuantity;
    private boolean isDeleted;
    //private boolean isHidden; 숨김처리 필요

    public Product(ProductRequestDto requestDto, UUID storeId) {
        this.storeId = storeId;
        this.productName = requestDto.getProductName();
        this.productPrice = (long) requestDto.getProductPrice();
        this.isDeleted = false;
    }

    public void upadte(UUID id, ProductRequestDto requestDto) {
        this.id = id;
        this.productName = requestDto.getProductName();
        this.productPrice = requestDto.getProductPrice();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateOrderCount(Long productQuantity) {
        this.productQuantity = productQuantity;
    }
}