package com.teamsparta14.order_service.product.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    ON_SALE("판매중"),
    SOLD_OUT("품절"),
    HIDDEN("숨김");

    private final String description;
}
