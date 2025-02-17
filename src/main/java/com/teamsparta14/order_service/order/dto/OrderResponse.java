package com.teamsparta14.order_service.order.dto;

import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.entity.OrderProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
public class OrderResponse {
    private UUID orderId;
    private Long userId ;
    private UUID storeId;
    private List<UUID> orderProducts;
    private UUID destId;
    private OrderType orderType;

    public static OrderResponse from(Order order){
        return OrderResponse.builder()
                .orderId(order.getOrder())
                .userId(order.getUserId())
                .storeId(order.getStoreId())
                .orderProducts(order.getOrderProducts().stream()
                        .map(OrderProduct::getProductId)
                        .toList())
                .destId(order.getDestId())
                .orderType(order.getOrderType())
                .build();

    }
}
