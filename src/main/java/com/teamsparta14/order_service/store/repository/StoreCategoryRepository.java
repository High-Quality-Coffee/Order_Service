package com.teamsparta14.order_service.store.repository;

import com.teamsparta14.order_service.store.entity.Store;
import com.teamsparta14.order_service.store.entity.StoreCategory;
import com.teamsparta14.order_service.store.entity.StoreCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, StoreCategoryId> {

    List<StoreCategory> findByStoreId(Store store);

    @Modifying
    @Transactional
    @Query("DELETE FROM StoreCategory sc WHERE sc.storeId = :store")
    void deleteByStore(Store store);
}
