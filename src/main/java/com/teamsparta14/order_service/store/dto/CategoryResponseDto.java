package com.teamsparta14.order_service.store.dto;

import com.teamsparta14.order_service.store.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private String id;
    private String categoryName;

    public CategoryResponseDto(Category category) {
        this.id = category.getId().toString();
        this.categoryName = category.getCategoryName();
    }
}
