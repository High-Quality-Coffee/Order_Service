package com.teamsparta14.order_service.store.repository;

import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreRegion;
import com.teamsparta14.order_service.store.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRegionRepository extends JpaRepository<StoreRegion, Long> {
    boolean existsByStoreIdAndRegionId(Store store, Region region);
}
