package com.teamsparta14.order_service.global.response;


import com.teamsparta14.order_service.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreClientResponse {
    String message;
    int status;
    Store data;
}
