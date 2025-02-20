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
@Table(name = "p_store")
public class Store extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "store_id")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 15)
    private String phone;

    private String createdBy;
    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    // 삭제 메서드
    public void deleteStore(String deletedBy) {
        setDeleted(java.time.LocalDateTime.now(), deletedBy);
        this.isDeleted = true;
    }
}
