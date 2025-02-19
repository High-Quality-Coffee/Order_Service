package com.teamsparta14.order_service.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressRequestDTO {

    @NotNull(message = "주소를 반드시 입력해야 합니다.")
    private String address;

    private String memo;

}
