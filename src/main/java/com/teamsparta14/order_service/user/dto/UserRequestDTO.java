package com.teamsparta14.order_service.user.dto;

import com.teamsparta14.order_service.global.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min= 4, max= 10, message = "아이디는 최소 4자 이상 최대 10자 이하입니다.")
    @Pattern(regexp = "^[a-z0-9]+$", message = "username은 알파벳 소문자(a~z)와 숫자(0~9)만 포함해야 합니다.")
    private String username;

    @Size(min = 8, max = 15, message = "password는 최소 8자 이상, 15자 이하이어야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임을 반드시 입력해주세요")
    private String userNickname;

}
