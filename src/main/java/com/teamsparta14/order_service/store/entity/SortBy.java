package com.teamsparta14.order_service.store.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum SortBy {
    CREATED_AT(Sort.by(Sort.Direction.DESC, "createdAt")),
    UPDATED_AT(Sort.by(Sort.Direction.DESC, "updatedAt"));

    private final Sort sort;
}

