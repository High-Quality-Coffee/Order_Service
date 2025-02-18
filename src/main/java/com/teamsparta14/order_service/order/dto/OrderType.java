package com.teamsparta14.order_service.order.dto;


import lombok.Getter;

@Getter
public enum OrderType {
    ONLINE,
    OFFLINE;

    private String type;

}