package com.teamsparta14.order_service.user.service;

import com.teamsparta14.order_service.global.enums.Role;
import com.teamsparta14.order_service.global.exception.BaseException;
import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.global.response.ResponseCode;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.dto.UserRequestDTO;
import com.teamsparta14.order_service.user.dto.UserResponseDTO;
import com.teamsparta14.order_service.user.entity.UserEntity;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import com.teamsparta14.order_service.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    //유저 회원가입
    public void user_save(UserRequestDTO userRequestDTO){
        ModelMapper modelMapper = new ModelMapper();

        //비밀번호 암호화
        userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);
        userEntity.setRole(Role.ROLE_USER);
        userRepository.save(userEntity);
    }

    //관리자 회원가입
    public void master_save(UserRequestDTO userRequestDTO){
        ModelMapper modelMapper = new ModelMapper();

        //비밀번호 암호화
        userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);
        userEntity.setRole(Role.ROLE_MASTER);
        userRepository.save(userEntity);
    }

    //가게주인 회원가입
    public void owner_save(UserRequestDTO userRequestDTO){
        ModelMapper modelMapper = new ModelMapper();

        //비밀번호 암호화
        userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);

        userEntity.setRole(Role.ROLE_OWNER);
        userRepository.save(userEntity);
    }

    //매니저 회원가입
    public void manager_save(UserRequestDTO userRequestDTO){
        ModelMapper modelMapper = new ModelMapper();

        //비밀번호 암호화
        userRequestDTO.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));

        UserEntity userEntity = modelMapper.map(userRequestDTO, UserEntity.class);
        userEntity.setRole(Role.ROLE_MANAGER);
        userRepository.save(userEntity);
    }

    //user 조회
    public ApiResponse<UserResponseDTO> findUser(String username){
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new BaseException("해당 유저를 찾을 수 없습니다."));
        return ApiResponse.success(modelMapper.map(userEntity,UserResponseDTO.class));
    }

    //회원 탈퇴 (soft-delete)
    @Transactional
    public void deleteUser(String token){
        String username = jwtUtil.getUsername(token);
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));
        userEntity.delete_user(true);
        userEntity.setDeleted(LocalDateTime.now(), username);
    }


}
