package com.teamsparta14.order_service.order.dto;


import com.teamsparta14.order_service.order.entity.OrderProduct;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
