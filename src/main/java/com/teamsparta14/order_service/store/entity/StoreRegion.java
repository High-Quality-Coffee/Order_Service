package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(StoreRegionId.class)
@Table(name = "p_store_region_category")
public class StoreRegion extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store storeId;

    @Id
    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region regionId;
}
