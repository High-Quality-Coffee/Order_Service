package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.store.dto.StoreUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
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

    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoreStatus status;

    @OneToMany(mappedBy = "storeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreCategory> storeCategories;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    private int totalReviewCount = 0;

//    private double totalReviewScore = 0;

    private double averageRating = 0;

    // 삭제
    public void deleteStore(String deletedBy) {
        setDeleted(java.time.LocalDateTime.now(), deletedBy);
        this.isDeleted = true;
    }

    // 업데이트
    public void update(StoreUpdateRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.address = requestDto.getAddress();
        this.phone = requestDto.getPhone();
    }

    // 리뷰 점수 업데이트
    public void updateRating(int newTotalReviewCount, double newAverageRating) {
        this.totalReviewCount = newTotalReviewCount;
        this.averageRating = newAverageRating;
    }
}
