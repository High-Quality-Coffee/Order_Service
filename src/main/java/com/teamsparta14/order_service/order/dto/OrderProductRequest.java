package com.teamsparta14.order_service.order.dto;


import com.teamsparta14.order_service.order.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductRequest {

    private UUID productId;
    private Long quantity;
    private Long price;

    public static OrderProductRequest from(OrderProduct orderProduct){

        return OrderProductRequest.builder()
                .productId(orderProduct.getProductId())
                .quantity(orderProduct.getQuantity())
                .price(orderProduct.getPrice())
                .build();

    }

}
