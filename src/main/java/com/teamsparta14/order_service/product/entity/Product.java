package com.teamsparta14.order_service.product.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@Table(name="p_product")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;*/

    @Builder.Default
    private Long storeId = 1L;
    private String productName;
    private int productPrice;
    private boolean isDeleted;

    public Product(ProductRequestDto requestDto, Long storeId) {
        this.storeId = storeId;
        this.productName = requestDto.getProduct_name();
        this.productPrice = requestDto.getProduct_price();
        this.isDeleted = false;
    }

    public void upadte(Long id, ProductRequestDto requestDto) {
        this.id = id;
        this.productName = requestDto.getProduct_name();
        this.productPrice = requestDto.getProduct_price();
    }

    public void delete() {
        this.isDeleted = true;
    }
}
