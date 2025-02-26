package com.teamsparta14.order_service.product.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @NotNull
    private UUID storeId;

    @NotNull
    private String productName;

    @NotNull
    @Min(0)
    @ColumnDefault("0")
    private Long productPrice;

    @NotNull
    @Min(0)
    @ColumnDefault("0")
    private Long productQuantity;

    @NotNull
    @ColumnDefault("false")
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductStatus status = ProductStatus.ON_SALE;

    @Column(length = 100)
    private String productDescription;

    public Product(ProductRequestDto requestDto, UUID storeId, String productDescription) {
        this.storeId = storeId;
        this.productName = requestDto.getProductName();
        this.productPrice = requestDto.getProductPrice();
        this.productQuantity = requestDto.getProductQuantity();
        this.isDeleted = false;
        this.status = ProductStatus.ON_SALE;
        this.productDescription = productDescription;
    }

    public void update(ProductRequestDto requestDto) {
        this.productName = requestDto.getProductName();
        this.productPrice = requestDto.getProductPrice();
        this.productQuantity = requestDto.getProductQuantity();
    }

    public void delete() {
        this.isDeleted = true;
    }

    public void updateOrderCount(Long productQuantity) {

        if (productQuantity < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");
        } else if (productQuantity == 0) {
            status = ProductStatus.SOLD_OUT;
        }

        this.productQuantity = productQuantity;
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }
}