package com.teamsparta14.order_service.order.service;


import com.teamsparta14.order_service.order.dto.*;
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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final StoresClient storesClient;
    private final JWTUtil jwtUtil;


    public OrderResponse createOrder(OrderCreateDto createDto,
                                     String token) {

        String userName = jwtUtil.getUsername(token);

        //dto 내부 storeId를 통해 store가 존재하는지 확인 구현 예정
        Optional.ofNullable(storesClient.searchStore(createDto.getStoreId().toString(), token))
                .orElseThrow(() -> new IllegalArgumentException("store Not found"));

        List<OrderProductRequest> orderProductRequests = createDto.getOrderProductRequests();

        List<UUID> productIds = orderProductRequests.stream().map(OrderProductRequest::getProductId).toList();

        List<ProductResponseDto> productResponses = productClient.searchProductList(productIds, token);
        requestCompareToClientProductList(orderProductRequests, productResponses);

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

    private void requestCompareToClientProductList(List<OrderProductRequest> orderProductRequests,
                                                   List<ProductResponseDto> productResponses) {

        Map<UUID, OrderProductRequest> productMap = orderProductRequests.stream()
                .collect(Collectors.toMap(
                        OrderProductRequest::getProductId,
                        Function.identity(),
                        (existing, replacement) -> replacement // 중복 키 처리
                ));

        for (ProductResponseDto product : productResponses) {
            OrderProductRequest request = productMap.get(product.getProductId());
            if (request == null || !compareOrderProductToClientProduct(request, product)) {
                throw new IllegalArgumentException("Not enough product in stock or product not found: " + product.getProductId());
            }
            productMap.remove(product.getProductId());
        }

        if (!productMap.isEmpty()) {
            StringBuilder missingProducts = new StringBuilder();
            productMap.keySet().forEach(productId -> missingProducts.append(productId).append(", "));
            throw new IllegalArgumentException("Products not found: " + missingProducts.toString());
        }
    }

    private boolean compareOrderProductToClientProduct(OrderProductRequest request, ProductResponseDto response) {

        if (request == null || response == null) {
            return false;
        }
        return request.getQuantity() <= response.getProductQuantity();
    }

    @Transactional
    public OrderResponse deleteOrder(UUID orderId, String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow(
                () -> new IllegalArgumentException("Order Not Found")
        );

        if (!order.isOwner(userName)) {
            throw new IllegalArgumentException("Not Own Order");
        }

        LocalDateTime orderTime = order.getCreatedAt();

        Duration duration = Duration.between(orderTime, LocalDateTime.now());

        if (duration.toMinutes() > 5) {
            throw new IllegalArgumentException("The order cancellation time has expired.");
        }

        order.setIsDeleted(true);

        return OrderResponse.from(order);
    }

    public OrderResponse getOrderById(UUID orderId, String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findOrderWithProductsWithPayment(orderId).orElseThrow(
                () -> new IllegalArgumentException("Order Not Found")
        );

        if (!order.isOwner(userName)) {
            throw new IllegalArgumentException("Not Own Order");
        }

        return OrderResponse.from(order);
    }

    public Page<OrderResponse> searchOrders(String token, int page, int limit,
                                            Boolean isAsc, String orderBy) {

        String userName = jwtUtil.getUsername(token);

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, orderBy));

        return orderRepository.searchByUserName(userName, pageable).map(OrderResponse::from);
    }

    @Transactional
    public OrderResponse updateOrder(OrderUpdateRequest orderUpdateRequest, String token) {

        String userName = jwtUtil.getUsername(token);

        MyOrder order = orderRepository.findById(orderUpdateRequest.getOrderId()).orElseThrow(
                () -> new IllegalArgumentException("Order Not Found")
        );

        if (!order.isOwner(userName)) {
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

    public Page<OrderResponse> searchOrdersByStoreId(String storeId ,String token, int page, int limit, Boolean isAsc, String orderBy) {

        String userName = jwtUtil.getUsername(token);

        Optional.ofNullable(storesClient.searchStore(storeId, token))
                .orElseThrow(() -> new IllegalArgumentException("store Not found"));

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, orderBy));

        return orderRepository.searchByStoreId(userName, pageable,storeId).map(OrderResponse::from);

    }

    public OrderResponse searchProduct(OrderSearchDto requestDto) {

        Optional<MyOrder> order = orderRepository.searchOrderById(requestDto.getRequestId());

        if(order.isEmpty()){
            throw new IllegalArgumentException("Not found Order");
        }
        return OrderResponse.from(order.get());
    }
}
