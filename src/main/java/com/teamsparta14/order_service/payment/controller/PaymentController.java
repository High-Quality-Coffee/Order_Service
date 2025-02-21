package com.teamsparta14.order_service.payment.controller;


import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.payment.dto.PaymentResponse;
import com.teamsparta14.order_service.payment.dto.PaymentUpdateDto;
import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.service.PaymentService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePayment(
            @RequestBody PaymentUpdateDto paymentUpdateDto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
            ){

        String userName = customUserDetails.getUsername();

        return ResponseEntity.ok(ApiResponse.success(paymentService.updatePayment(paymentUpdateDto,userName)));
    }

    @GetMapping("/{payment_id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable(name = "payment_id") UUID paymentId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){

        String userName = customUserDetails.getUsername();
        return ResponseEntity.ok(ApiResponse.success(paymentService.getPayment(paymentId , userName)));
    }
}
