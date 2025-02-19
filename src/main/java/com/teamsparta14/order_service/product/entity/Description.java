package com.teamsparta14.order_service.product.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_description")
public class Description {

    @Id
    @UuidGenerator
    private UUID id;

    @NotNull
    @Column(name = "request" ,length = 1000)
    private String request;
    @Column(name = "response" , length = 1000)
    private String response;
}
