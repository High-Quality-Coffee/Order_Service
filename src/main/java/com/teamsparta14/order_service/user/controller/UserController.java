package com.teamsparta14.order_service.user.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import com.teamsparta14.order_service.user.dto.UserRequestDTO;
import com.teamsparta14.order_service.user.dto.UserResponseDTO;
import com.teamsparta14.order_service.user.entity.UserEntity;
import com.teamsparta14.order_service.user.service.TokenReissueService;
import com.teamsparta14.order_service.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenReissueService tokenReissueService;

    //유저 회원가입
    @PostMapping("/api/auth/join/user")
    public void user_join(@RequestBody @Valid UserRequestDTO userRequestDTO){
        userService.user_save(userRequestDTO);
    }

    //관리자 회원가입
    @PostMapping("/api/auth/join/master")
    public void master_join(@RequestBody @Valid UserRequestDTO userRequestDTO) {userService.master_save(userRequestDTO);}

    //가게주인 회원가입
    @PostMapping("/api/auth/join/owner")
    public void owner_join(@RequestBody @Valid UserRequestDTO userRequestDTO) {userService.owner_save(userRequestDTO);}

    //매니저 회원가입
    @PostMapping("/api/auth/join/manager")
    public void manager_join(@RequestBody @Valid UserRequestDTO userRequestDTO) {userService.manager_save(userRequestDTO);}

    //유저 조회
    @GetMapping("/api/user/list/{username}")
    public UserResponseDTO user_list(@PathVariable("username") String username) {
        return userService.findUser(username);
    }

    //토큰 재발급
    @PostMapping("/api/auth/token/reissue")
    public ResponseEntity<ApiResponse<String>> reissue(HttpServletRequest request, HttpServletResponse response){
        return tokenReissueService.token_reissue(request,response);
    }


}
