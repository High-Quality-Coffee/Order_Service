package com.teamsparta14.order_service.payment.controller;


import com.teamsparta14.order_service.global.response.ApiResponse;
import com.teamsparta14.order_service.payment.dto.PaymentResponse;
import com.teamsparta14.order_service.payment.dto.PaymentUpdateDto;
import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.service.PaymentService;
import com.teamsparta14.order_service.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;


    @Operation(summary = "결제 수정", description = "결제 수정 API")
    @Secured({"ROLE_OWNER"})
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> updatePayment(
            @RequestBody PaymentUpdateDto paymentUpdateDto,
           @RequestHeader("access") String token
            ){


        return ResponseEntity.ok(ApiResponse.success(paymentService.updatePayment(paymentUpdateDto,token)));
    }
    @Operation(summary = "결제 조회", description = "payment_id 통해결제 API")
    @Secured({"ROLE_OWNER","ROLE_MASTER"})
    @GetMapping("/{payment_id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable(name = "payment_id") UUID paymentId,
            @RequestHeader("access") String token
    ){

        return ResponseEntity.ok(ApiResponse.success(paymentService.getPayment(paymentId , token)));
    }
}
