package com.teamsparta14.order_service.order.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.dto.OrderUpdateRequest;
import com.teamsparta14.order_service.order.service.OrderService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @RequestBody OrderCreateDto createDto,
            @RequestHeader(name = "access") String token
    ){

        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(createDto ,token)));
    }

    @DeleteMapping(path = "/{order-id}")
    public ResponseEntity<ApiResponse<OrderResponse>> deleteOrder(
            @PathVariable(name = "order-id") UUID orderId,
            @RequestHeader(name = "access") String token) {


        log.info("orderId : {}", orderId);
        return ResponseEntity.ok(ApiResponse.success(orderService.deleteOrder(orderId, token)));
    }

    @GetMapping(path = "/{order-id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable(name = "order-id") UUID orderId,
            @RequestHeader(name = "access") String token){

        log.info("orderId : {}",orderId);

        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(orderId,token)));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByPage(
            @RequestHeader(name = "access") String token,
            @RequestParam(name = "page", defaultValue = "10") int page,
            @RequestParam(name = "limit", defaultValue = "100") int limit,
            @RequestParam(name = "isAsc" , defaultValue = "true") Boolean isAsc,
            @RequestParam(name = "orderBy") String orderBy){


        return ResponseEntity.ok(ApiResponse.success(orderService.searchOrders(token,page,limit,isAsc,orderBy)));
    }


    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(
            @RequestBody OrderUpdateRequest orderUpdateRequest,
            @RequestHeader(name = "access") String token
    ){

        return ResponseEntity.ok(orderService.updateOrder(orderUpdateRequest,token));
    }
}
