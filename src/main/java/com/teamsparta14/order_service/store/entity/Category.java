package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_category")
public class Category extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "category_id")
    private UUID id;

    @Column(name = "category_name", nullable = false, length = 20)
    private String categoryName;
}
