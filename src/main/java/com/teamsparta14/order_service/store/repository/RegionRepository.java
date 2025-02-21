package com.teamsparta14.order_service.store.repository;


import com.teamsparta14.order_service.store.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RegionRepository extends JpaRepository<Region, UUID> {
    Optional<Region> findByRegionName(String regionName);
    boolean existsByRegionName(String regionName);
}

