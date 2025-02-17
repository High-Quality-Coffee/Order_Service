package com.teamsparta14.order_service.order.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_order_product")
public class OrderProduct {



    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_product_id")
    private UUID order_product_id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "price")
    private Long price;

}
