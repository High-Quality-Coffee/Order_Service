package com.teamsparta14.order_service.payment.service;

import com.teamsparta14.order_service.payment.dto.PaymentUpdateDto;
import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment updatePayment(PaymentUpdateDto paymentUpdateDto, String userName) {

        Payment payment  = paymentRepository.findById(paymentUpdateDto.getPaymentId()).orElseThrow(
                () -> new IllegalArgumentException("Not found Payment")
        );

        if (!userName.equals(payment.getUserName())){
            throw new IllegalArgumentException("Not Own Payment");
        }

        payment.setPaymentStatus(paymentUpdateDto.getPaymentStatus());

        return payment;
    }

    public Payment getPayment(UUID paymentId, String userName) {
        Payment payment  = paymentRepository.findById(paymentId).orElseThrow(
                () -> new IllegalArgumentException("Not found Payment")
        );

        if (!userName.equals(payment.getUserName())){
            throw new IllegalArgumentException("Not Own Payment");
        }

        return payment;

    }
}
