package com.teamsparta14.order_service.order.repository;


import com.teamsparta14.order_service.order.entity.Order;
import io.micrometer.observation.ObservationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderCustomRepository {


    @Override
    public Page<Order> searchByUserName(String userName, Pageable pageable) {
        return null;
    }
}
