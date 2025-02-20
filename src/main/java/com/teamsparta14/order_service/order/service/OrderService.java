package com.teamsparta14.order_service.order.service;


import com.teamsparta14.order_service.global.response.ProductClientResponse;
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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


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

        List<ProductResponseDto> productClientResponse= productClient.searchProductList(orderProductRequests,token);

        requestCompareToProductList(orderProductRequests , productClientResponse);

        Order order = createDto.from(userName);
         for (OrderProductRequest orderProductRequest : orderProductRequests) {


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

    private void requestCompareToProductList(List<OrderProductRequest> orderProductRequests, List<ProductResponseDto> clienList) {

        //client를 통해 얻은 데이터를 Map으로
        Map<UUID, Long> productMap = clienList.stream()
                .collect(Collectors.toMap(ProductResponseDto::getProductId,ProductResponseDto::getProductQuantity));

        for (OrderProductRequest orderProductRequest : orderProductRequests) {
            Long productQuantity = productMap.get(orderProductRequest.getProductId());

            if (productQuantity < orderProductRequest.getQuantity()) {
                throw new IllegalArgumentException("Not enough product in stock");
            }
            productMap.remove(orderProductRequest.getProductId());
        }

        if(!productMap.isEmpty()){
            throw new IllegalArgumentException("product not found");
        }

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