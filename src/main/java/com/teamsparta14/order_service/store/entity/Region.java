package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_region")
public class Region extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "region_id")
    private UUID id;

    @Column(name = "region_name", nullable = false, unique = true, length = 50)
    private String regionName;
}
