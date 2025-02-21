package com.teamsparta14.order_service.order.dto;


import com.teamsparta14.order_service.order.entity.MyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderCreateDto {

    private UUID storeId;
    private UUID destId;
    private List<OrderProductRequest> orderProductRequests;
    private OrderType orderType;
    private String orderComment;

    public MyOrder from(String userName){
        return MyOrder.builder()
                .userName(userName)
                .storeId(storeId)
                .isDeleted(false)
                .orderComment(orderComment)
                .orderProducts(new ArrayList<>())
                .destId(destId)
                .orderType(OrderType.ONLINE)
                .build();
    }
}
