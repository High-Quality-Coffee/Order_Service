package com.teamsparta14.order_service.order.entity;


import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_payment")
public class Payment extends BaseEntity {

    @Id
    @UuidGenerator
    private UUID paymentId;

    @OneToOne(mappedBy = "payment")
    private Order order;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


}
