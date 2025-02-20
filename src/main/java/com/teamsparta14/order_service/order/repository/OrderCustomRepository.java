package com.teamsparta14.order_service.order.repository;

import com.teamsparta14.order_service.order.entity.MyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {
    Page<MyOrder> searchByUserName(String userName, Pageable pageable);
}
