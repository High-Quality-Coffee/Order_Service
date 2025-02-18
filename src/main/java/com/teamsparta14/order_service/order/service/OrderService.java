package com.teamsparta14.order_service.order.service;


import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderProductRequest;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.dto.OrderUpdateRequest;
import com.teamsparta14.order_service.order.entity.Order;
import com.teamsparta14.order_service.order.entity.OrderProduct;
import com.teamsparta14.order_service.order.repository.OrderRepository;
import com.teamsparta14.order_service.order.repository.ProductClient;
import com.teamsparta14.order_service.product.dto.ProductListResponseDto;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {
//
//    private final StoresClient storesClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;


    public OrderResponse createOrder(OrderCreateDto createDto , String userName,String token) {

        //dto 내부 storeId를 통해 store가 존재하는지 확인 구현 예정

        List<OrderProductRequest> orderProductRequests = createDto.getOrderProductRequests();

        ProductListResponseDto responseDtoList = productClient.searchProductList(orderProductRequests,token);

        Order order = createDto.from(userName);
         for (OrderProductRequest orderProductRequest : orderProductRequests) {




//             if(product.getQuantity() < orderProductRequest.getQuantity()){
//                 throw new IllegalArgumentException("Not Enough Product");
//             }
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


    @Transactional
    public OrderResponse deleteOrder(UUID orderId,String userName) {


        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        // 유저 인증 추가 구현 필요
        if(!order.getUserName().equals(userName)){
            throw new IllegalArgumentException("Not Own Order");
        }

        orderRepository.delete(order);

        return OrderResponse.from(order);
    }

    public OrderResponse getOrderById(UUID orderId,String userName) {
        ;

        Order order = orderRepository.findById(orderId).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        // 유저 인증 추가 구현 필요
        if(!order.getUserName().equals(userName)){
            throw new IllegalArgumentException("Not Own Order");
        }

        // 유저 인증 추가 구현 필요

        return OrderResponse.from(order);
    }

    public List<OrderResponse> searchOrders(String userName, int page, int limit,
                                          Boolean isAsc, String orderBy) {
        Sort.Direction direction;
        if(isAsc){
            direction = Sort.Direction.ASC;
        }else {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page-1, limit, Sort.by(direction, orderBy));

        Page<OrderResponse> orderPage = orderRepository.searchByUserId(userName,pageable).map(OrderResponse::from);

        return orderPage.toList();
    }

    @Transactional
    public OrderResponse updateOrder(OrderUpdateRequest orderUpdateRequest, String userName) {

        Order order = orderRepository.findById(orderUpdateRequest.getOrderId()).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        if(!order.getUserName().equals(userName)){
            throw new IllegalArgumentException("Not Own Order");
        }

        List<OrderProduct> updateList = new ArrayList<>();

        for (OrderProductRequest orderProductRequest : orderUpdateRequest.getOrderProductRequests()) {
            updateList.add(OrderProduct.builder()
                    .order(order)
                    .productId(orderProductRequest.getProductId())
                    .quantity(orderProductRequest.getQuantity())
                    .price(orderProductRequest.getPrice())
                    .build());
        }

        order.updateOrderProductList(updateList);

        return OrderResponse.from(order);
    }
}
