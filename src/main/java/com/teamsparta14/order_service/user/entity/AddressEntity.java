package com.teamsparta14.order_service.user.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "p_address")
public class AddressEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private UserEntity user;

    @Column(name="address" ,nullable = false)
    private String address;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;
}
