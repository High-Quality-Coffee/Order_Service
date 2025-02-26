package com.teamsparta14.order_service.payment.entity;


import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.order.entity.MyOrder;
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
    private MyOrder order;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


}
