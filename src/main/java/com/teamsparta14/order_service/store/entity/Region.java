package com.teamsparta14.order_service.store.entity;

import com.teamsparta14.order_service.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
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

    @Column(name = "region_name", nullable = false, length = 50, unique = true)
    private String regionName;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores;
}

