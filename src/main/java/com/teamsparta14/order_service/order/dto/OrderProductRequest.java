package com.teamsparta14.order_service.order.dto;


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
}
