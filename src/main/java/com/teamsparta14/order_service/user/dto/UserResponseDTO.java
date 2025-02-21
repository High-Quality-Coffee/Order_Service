package com.teamsparta14.order_service.user.dto;

import com.teamsparta14.order_service.global.enums.Role;
import lombok.Data;

@Data
public class UserResponseDTO {
    private String username;
    private String userNickname;
    private Role role;
}
