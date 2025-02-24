package com.teamsparta14.order_service.order.repository;

import com.teamsparta14.order_service.order.dto.OrderSearchDto;
import com.teamsparta14.order_service.order.entity.MyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderCustomRepository {
    Page<MyOrder> searchByUserName(String userName, Pageable pageable);

    Page<MyOrder> searchByStoreId(String userName, Pageable pageable, String storeId);

    List<MyOrder> searchOrderByIdList(OrderSearchDto requestDto);
}
