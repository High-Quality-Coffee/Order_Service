package com.teamsparta14.order_service.order.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ApiResponse<OrderResponse> createOrder(
            @RequestBody OrderCreateDto createDto,
            @RequestHeader("user-id") Long userId){ // 나중에 토큰으로 변경

        return ApiResponse.success(orderService.createOrder(createDto, userId));
    }
}
