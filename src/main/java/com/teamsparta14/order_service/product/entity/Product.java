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
    private int productPrice;
    private boolean isDeleted;

    public Product(ProductRequestDto requestDto, UUID storeId) {
        this.storeId = storeId;
        this.productName = requestDto.getProduct_name();
        this.productPrice = requestDto.getProduct_price();
        this.isDeleted = false;
    }

    public void upadte(UUID id, ProductRequestDto requestDto) {
        this.id = id;
        this.productName = requestDto.getProduct_name();
        this.productPrice = requestDto.getProduct_price();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
