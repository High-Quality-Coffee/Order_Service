package com.teamsparta14.order_service.order.service;


import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderProductRequest;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.entity.OrderProduct;
import com.teamsparta14.order_service.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
//
//    private final StoresClient storesClient;
//    private final ProductClient productClient;
    private final OrderRepository orderRepository;


    public OrderResponse createOrder(OrderCreateDto createDto , Long userId) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인 구현 예정

        List<OrderProductRequest> orderProductRequests = createDto.getOrderProductRequests();

        Order order = createDto.from(userId);
         for (OrderProductRequest orderProductRequest : orderProductRequests) {

             //수량 체크 구현 예정
             //이때 storeid와 productId를 사용하여 productClient 통해 http 통신으로 구현

             //고민 되는 점  :  물품이 100개이면 100개의 통신을 해야하는데 너무 별로임;;
             // productIdList와 storeId를 통해 한번에 구현?


             order.addOrderProductsList(OrderProduct.builder()
                             .order(order)
                             .productId(orderProductRequest.getProductId())
                             .quantity(orderProductRequest.getQuantity())
                             .price(orderProductRequest.getPrice())
                     .build());
         }

         order.createPayment();

         return OrderResponse.from(orderRepository.save(order));
    }

}
