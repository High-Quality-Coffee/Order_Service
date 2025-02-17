package com.teamsparta14.order_service.order.dto;


import com.teamsparta14.order_service.order.entity.Order;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCreateDto {

    private UUID storeId;
    private UUID destId;
    private List<OrderProductRequest> orderProductRequests;
    private OrderType orderType;

    public Order from(Long userId){
        return Order.builder()
                .userId(userId)
                .storeId(storeId)
                .orderProducts(new ArrayList<>())
                .destId(destId)
                .orderType(OrderType.ONLINE)
                .build();
    }
}
