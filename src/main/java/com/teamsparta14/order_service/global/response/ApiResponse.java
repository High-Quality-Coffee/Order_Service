package com.teamsparta14.order_service.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String message;
    private int status;
    private T data;

    private static final int SUCCESS = 200;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>( "success", SUCCESS, data);
    }

    public static <T> ApiResponse<T> fail(ResponseCode responseCode,T data) {
        return new ApiResponse<T>(responseCode.getMessage(), responseCode.getHttpStatusCode(), data);
    }
}
