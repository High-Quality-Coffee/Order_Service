package com.teamsparta14.order_service.user.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "p_address")
public class AddressEntity extends BaseEntity {
    @Id
    @UuidGenerator
    @Column
    private UUID id;

    @Column(length = 10)  // ✅ FK로 사용될 username 저장
    private String username;

    @Column(name="address" ,nullable = false)
    private String address;

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;
}