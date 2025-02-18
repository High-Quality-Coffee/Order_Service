package com.teamsparta14.order_service.user.repository;

import com.teamsparta14.order_service.user.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
    List<AddressEntity> findAllByUsername(String username);
}
