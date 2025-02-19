package com.teamsparta14.order_service.review.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Stars {
    ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int value;
}
