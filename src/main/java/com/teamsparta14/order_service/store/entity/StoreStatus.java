package com.teamsparta14.order_service.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoreStatus {
    OPEN("운영중"),
    HIDDEN("숨김");

    private final String description;
}

