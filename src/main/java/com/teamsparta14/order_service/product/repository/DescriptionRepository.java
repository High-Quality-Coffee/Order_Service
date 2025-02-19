package com.teamsparta14.order_service.product.repository;

import com.teamsparta14.order_service.product.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DescriptionRepository extends JpaRepository<Description, UUID> {
}
