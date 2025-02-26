package com.teamsparta14.order_service.order.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderUpdateRequest {
    private UUID orderId;
    private List<OrderProductRequest> orderProductRequests;
}