package com.teamsparta14.order_service.global.response;


import com.teamsparta14.order_service.product.dto.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductClientResponse {
    String message;
    int status;
    List<ProductResponseDto> data;
}
