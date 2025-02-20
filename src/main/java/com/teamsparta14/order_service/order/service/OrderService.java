package com.teamsparta14.order_service.order.service;


import com.teamsparta14.order_service.order.dto.OrderCreateDto;
import com.teamsparta14.order_service.order.dto.OrderProductRequest;
import com.teamsparta14.order_service.order.dto.OrderResponse;
import com.teamsparta14.order_service.order.dto.OrderUpdateRequest;
import com.teamsparta14.order_service.order.entity.MyOrder;
import com.teamsparta14.order_service.order.entity.OrderProduct;
import com.teamsparta14.order_service.order.repository.OrderRepository;
import com.teamsparta14.order_service.order.repository.ProductClient;
import com.teamsparta14.order_service.order.repository.StoresClient;
import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final StoresClient storesClient;
    private final JWTUtil jwtUtil;


    public OrderResponse createOrder(OrderCreateDto createDto ,String token) {

        String userName = jwtUtil.getUsername(token);

        //dto 내부 storeId를 통해 store가 존재하는지 확인 구현 예정
        Object store = storesClient.searchStore(createDto.getStoreId(),token);

        if(null == store){
            throw new IllegalArgumentException("store Not found");
        }

        List<OrderProductRequest> orderProductRequests = createDto.getOrderProductRequests();

        List<ProductResponseDto> productClientResponse = productClient.searchProductList(orderProductRequests, token);


        requestCompareToClientProductList(orderProductRequests,productClientResponse);


        MyOrder order = createDto.from(userName);
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

    private void requestCompareToClientProductList(List<OrderProductRequest> orderProductRequests, List<ProductResponseDto> productClientResponse) {


        Map<UUID,OrderProductRequest> productMap = createOrderProductMap(orderProductRequests);

        for (ProductResponseDto product : productClientResponse) {
            if (!compareOrderProductToClientProduct(productMap.get(product.getProductId()), product)) {
                throw new IllegalArgumentException("Not enough product in stock");
            }
            productMap.remove(product.getProductId());
        }

        if (!productMap.isEmpty()) {
            throw new IllegalArgumentException("product not found");
        }

    }

    private boolean compareOrderProductToClientProduct(OrderProductRequest orderProduct, ProductResponseDto clientProduct) {

        if(orderProduct.getQuantity() > clientProduct.getProductQuantity()) return false;

        if(orderProduct.getPrice().equals(clientProduct.getProductPrice())) return false;

        return true;
    }



    private Map<UUID,OrderProductRequest> createOrderProductMap(List<OrderProductRequest> orderProductRequests){

        Map<UUID,OrderProductRequest> list = new HashMap<>();

        orderProductRequests.forEach(product -> {
            list.put(product.getProductId(),product);
        });

       return list;
    }


    @Transactional
    public OrderResponse deleteOrder(UUID orderId,String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findByOrderIdAndDeletedAtIsNull(orderId).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        if( !order.isOwner(userName) ){
            throw new IllegalArgumentException("Not Own Order");
        }

        LocalDateTime orderTime = order.getCreatedAt();

        Duration duration = Duration.between(orderTime, LocalDateTime.now());

        if(duration.toMinutes() > 5){
            throw new IllegalArgumentException("The order cancellation time has expired.");
        }

        order.setDeleted(LocalDateTime.now(),userName);

        return OrderResponse.from(order);
    }

    public OrderResponse getOrderById(UUID orderId,String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findOrderWithProductsWithPayment(orderId).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        if( !order.isOwner(userName) ){
            throw new IllegalArgumentException("Not Own Order");
        }

        return OrderResponse.from(order);
    }

    public List<OrderResponse> searchOrders(String token, int page, int limit,
                                          Boolean isAsc, String orderBy) {

        String userName = jwtUtil.getUsername(token);

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page-1, limit, Sort.by(direction, orderBy));

        Page<OrderResponse> orderPage = orderRepository.searchByUserName(userName,pageable).map(OrderResponse::from);

        return orderPage.toList();
    }

    @Transactional
    public OrderResponse updateOrder(OrderUpdateRequest orderUpdateRequest, String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findById(orderUpdateRequest.getOrderId()).orElseThrow(
                ()-> new IllegalArgumentException("Order Not Found")
        );

        if( !order.isOwner(userName) ){
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
