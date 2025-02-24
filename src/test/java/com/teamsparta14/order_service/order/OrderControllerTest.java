package com.teamsparta14.order_service.order;


import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.order.controller.OrderController;
import com.teamsparta14.order_service.order.dto.*;
import com.teamsparta14.order_service.order.service.OrderService;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Value("${spring.jwt.secret}")
    private String secret;

    private Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

    private UUID storeId = UUID.randomUUID();

    private UUID destId = UUID.randomUUID();

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName(" 주문 생성 테스트")
    void getMemberListTest() throws Exception {

        OrderCreateDto createDto = new OrderCreateDto();

        createDto.setStoreId(storeId);
        createDto.setDestId(destId);
        createDto.getOrderProductRequests();
        createDto.setOrderType(OrderType.ONLINE);
        createDto.setOrderComment("테스트 입니다");



        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("role", Role.ROLE_MANAGER)
                .claim("userName", "testUser")
                .compact();


        verify(orderService).createOrder(createDto, token);
    }

}