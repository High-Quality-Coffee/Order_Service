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

    public Order from(String userName){
        return Order.builder()
                .userName(userName)
                .storeId(storeId)
                .orderProducts(new ArrayList<>())
                .destId(destId)
                .orderType(OrderType.ONLINE)
                .build();
    }
}
