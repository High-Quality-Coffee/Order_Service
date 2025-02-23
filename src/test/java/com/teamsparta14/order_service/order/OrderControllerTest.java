package com.teamsparta14.order_service.order;


import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.controller.OrderController;
import com.teamsparta14.order_service.order.dto.*;
import com.teamsparta14.order_service.order.service.OrderService;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderCreateDto createDto;
    private OrderResponse orderResponse;
    private OrderUpdateRequest orderUpdateRequest;
//    private UUID orderId = St
    private String token;
    private UUID storeId;
    private JWTUtil jwtUtil = new JWTUtil("asdfasweqthyhtbiuihgoiqeirughjadxklzjhbKDFjhruewgDFuejdfvxcdsdf");


    @BeforeEach
    void setUp() {
        createDto = new OrderCreateDto();
        orderUpdateRequest = new OrderUpdateRequest();
        token = jwtUtil.createJwt("category", "testUser", Role.ROLE_MASTER.toString(), 10_000L);
        storeId = UUID.randomUUID();
    }

    @Test
    void createOrder_Success() {

        List<OrderProductRequest> orderList = new ArrayList<>();

        OrderProductRequest orderProductRequest = OrderProductRequest.builder()
                .productId(UUID.randomUUID())
                .price(4000L)
                .quantity(2L)
                .build();


        orderList.add(orderProductRequest);

        createDto.setDestId(UUID.randomUUID());
        createDto.setOrderProductRequests(orderList);
        createDto.setOrderComment("테스트 주문입니다");
        createDto.setOrderType(OrderType.ONLINE);
        createDto.setStoreId(storeId);

        when(orderService.createOrder(createDto, token)).thenReturn(orderResponse);

        ResponseEntity<ApiResponse<OrderResponse>> response = orderController.createOrder(createDto, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(orderResponse, response.getBody().getData());
    }

}