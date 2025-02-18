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
            @AuthenticationPrincipal CustomUserDetails customUserDetails){

        String user = customUserDetails.getUsername();
        log.info("user : {}",user);
        return ResponseEntity.ok(ApiResponse.success(orderService.createOrder(createDto, user)));
    }

    @DeleteMapping(path = "/{order-id}")
    public ResponseEntity<ApiResponse<OrderResponse>> deleteOrder(
            @PathVariable(name = "order-id") UUID orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String user = customUserDetails.getUsername();
        log.info("user : {}",user);
        log.info("orderId : {}",orderId);
        return ResponseEntity.ok(ApiResponse.success(orderService.deleteOrder(orderId,user)));
    }

    @GetMapping(path = "/{order-id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable(name = "order-id") UUID orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String user = customUserDetails.getUsername();
        log.info("user : {}",user);
        log.info("orderId : {}",orderId);

        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(orderId,user)));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrderByPage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "isAsc") Boolean isAsc,
            @RequestParam(name = "orderBy") String orderBy){


        return ResponseEntity.ok(ApiResponse.success(orderService.searchOrders(customUserDetails.getUsername(),page,limit,isAsc,orderBy)));
    }


    @PutMapping
    public ResponseEntity<OrderResponse> updateOrder(
            @RequestBody OrderUpdateRequest orderUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        String userName = customUserDetails.getUsername();
        return ResponseEntity.ok(orderService.updateOrder(orderUpdateRequest, userName));
    }
}
