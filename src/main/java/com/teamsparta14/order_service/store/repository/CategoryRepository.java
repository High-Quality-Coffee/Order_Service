package com.teamsparta14.order_service.store.repository;

import com.teamsparta14.order_service.store.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface
CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByCategoryNameIn(List<String> categoryNames);

    boolean existsByCategoryName(String categoryName);
}
