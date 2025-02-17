package com.teamsparta14.order_service.order.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @DeleteMapping(path = "/{order-id}")
    public ApiResponse<OrderResponse> deleteOrder(
            @PathVariable(name = "order-id") UUID orderId){

        return ApiResponse.success(orderService.deleteOrder(orderId));
    }

    @GetMapping(path = "/{order-id}")
    public ApiResponse<OrderResponse> getOrderById(
            @PathVariable(name = "order-id") UUID orderId){

        return ApiResponse.success(orderService.getOrderById(orderId));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getOrderByPage(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "isAsc") Boolean isAsc,
            @RequestParam(name = "orderBy") String orderBy){

        return ApiResponse.success(orderService.searchOrders(userId,page,limit,isAsc,orderBy));
    }
}
