package com.teamsparta14.order_service.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StoreCategoryId.class) // 복합 키 설정
@Table(name = "p_store_category")
public class StoreCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = true, length = 100)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = true, length = 100)
    private String updatedBy;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", nullable = true, length = 100)
    private String deletedBy;

    // 복합 키를 받는 생성자 추가
    public StoreCategory(Store store, Category category) {
        this.store = store;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.createdBy = "system"; // 변경 예정
    }
}
