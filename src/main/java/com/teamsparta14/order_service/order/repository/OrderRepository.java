package com.teamsparta14.order_service.order.repository;


import com.teamsparta14.order_service.order.entity.MyOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface OrderRepository extends JpaRepository<MyOrder, UUID>, OrderCustomRepository {
    Optional<MyOrder> findByOrderIdAndDeletedAtIsNull(UUID orderId);

    @Query("SELECT o FROM MyOrder o JOIN FETCH o.payment JOIN FETCH o.orderProducts WHERE o.orderId = :orderId")
    Optional<MyOrder> findOrderWithProductsWithPayment(@Param("orderId") UUID orderId);
}