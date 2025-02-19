package com.teamsparta14.order_service.product.repository;

import com.teamsparta14.order_service.product.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DescriptionRepository extends JpaRepository<Description, UUID> {
}
