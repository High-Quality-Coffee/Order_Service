package com.teamsparta14.order_service.payment.dto;


import com.teamsparta14.order_service.payment.entity.Payment;
import com.teamsparta14.order_service.payment.entity.PaymentStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    UUID paymentId;
    PaymentStatus paymentStatus;
    Long amount;

    public static PaymentResponse from(Payment payment) {
        return builder()
                .amount(payment.getAmount())
                .paymentStatus(payment.getPaymentStatus())
                .paymentId(payment.getPaymentId())
                .build();
    }
}
