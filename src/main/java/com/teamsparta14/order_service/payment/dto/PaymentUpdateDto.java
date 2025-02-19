package com.teamsparta14.order_service.payment.dto;

import com.teamsparta14.order_service.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUpdateDto {

    private UUID paymentId;
    private PaymentStatus paymentStatus;
}
