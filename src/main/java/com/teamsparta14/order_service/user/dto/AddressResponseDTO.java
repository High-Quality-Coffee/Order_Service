package com.teamsparta14.order_service.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class AddressResponseDTO {
    private UUID id;
    private String address;
    private String memo;
}
