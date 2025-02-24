package com.teamsparta14.order_service.global.response;


import com.teamsparta14.order_service.order.dto.OrderResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderClientResponse {
    String message;
    int status;
    List<OrderResponse> data;
}
