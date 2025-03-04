package com.teamsparta14.order_service.payment.service;

import com.teamsparta14.order_service.payment.dto.PaymentResponse;
import com.teamsparta14.order_service.payment.dto.PaymentUpdateDto;
import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.repository.PaymentRepository;
import com.teamsparta14.order_service.user.jwt.JWTUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final JWTUtil jwtUtil;

    @Transactional
    public PaymentResponse updatePayment(PaymentUpdateDto paymentUpdateDto, String token) {

        String userName = jwtUtil.getUsername(token);

        Payment payment  = getPaymentByPaymentId(paymentUpdateDto.getPaymentId(),userName);

        payment.setPaymentStatus(paymentUpdateDto.getPaymentStatus());

        return PaymentResponse.from(payment);
    }

    public PaymentResponse getPayment(UUID paymentId, String token) {

        String userName = jwtUtil.getUsername(token);

        Payment payment = getPaymentByPaymentId(paymentId,userName);

        return PaymentResponse.from(payment);

    }

    public Payment getPaymentByPaymentId(UUID paymentId, String userName){
        Payment payment  = paymentRepository.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException("Not found Payment")
        );

        if (!userName.equals(payment.getUserName())){
            throw new IllegalArgumentException("Not Own Payment");
        }
        return payment;
    }
}
