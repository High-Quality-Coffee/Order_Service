package com.teamsparta14.order_service.user.controller;

import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.user.dto.AddressRequestDTO;
import com.teamsparta14.order_service.user.dto.AddressResponseDTO;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import com.teamsparta14.order_service.user.repository.AddressRepository;
import com.teamsparta14.order_service.user.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    //주소 입력
    @PostMapping("/api/address")
    public void create_address(@RequestHeader(name = "access") String token, @RequestBody @Valid AddressRequestDTO addressRequestDTO){
        addressService.save_address(token,addressRequestDTO);
    }

    //주소 읽기
    @GetMapping("/api/address")
    public ResponseEntity<ApiResponse<List<AddressResponseDTO>>> read_address(@RequestHeader(name = "access") String token){
        ApiResponse<List<AddressResponseDTO>> addressResponseDTOS = addressService.read_address(token);
        return ResponseEntity.ok().body(addressResponseDTOS);
    }

    //주소 수정
    @PutMapping("/api/address/{address_id}")
    public ResponseEntity<ApiResponse<String>> update_address(@RequestHeader(name = "access") String token, @RequestBody @Valid AddressRequestDTO addressRequestDTO, @PathVariable("address_id") UUID address_id){
        ApiResponse<String> apiResponse = addressService.update_address(token,addressRequestDTO,address_id);
        return ResponseEntity.ok().body(apiResponse);
    }

    //주소 삭제
    @DeleteMapping("/api/address/{address_id}")
    public ResponseEntity<ApiResponse<String>> delete_address(@RequestHeader(name = "access") String token, @PathVariable("address_id") UUID address_id){
        ApiResponse<String> apiResponse = addressService.delete_address(token, address_id);
        return ResponseEntity.ok().body(apiResponse);
    }


}
