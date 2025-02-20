package com.teamsparta14.order_service.order.dto;

import com.teamsparta14.order_service.order.entity.MyOrder;
import com.teamsparta14.order_service.payment.dto.PaymentResponse;
import com.teamsparta14.order_service.payment.entity.Payment;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
public class OrderResponse {
    private UUID orderId;
    private String userName ;
    private UUID storeId;
    private List<OrderProductRequest> orderProducts;
    private UUID destId;
    private OrderType orderType;
    private PaymentResponse paymentResponse;

    public static OrderResponse from(MyOrder order){
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userName(order.getUserName())
                .storeId(order.getStoreId())
                .orderProducts(order.getOrderProducts().stream().map(
                        OrderProductRequest::from
                ).toList())
                .destId(order.getDestId())
                .orderType(order.getOrderType())
                .paymentResponse(PaymentResponse.from(order.getPayment()))
                .build();

    }
}
