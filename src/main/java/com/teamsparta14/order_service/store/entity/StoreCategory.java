package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 필수
@Builder
@IdClass(StoreCategoryId.class) // 복합 키 설정
@Table(name = "p_store_category")
public class StoreCategory extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store storeId;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category categoryId;

}
