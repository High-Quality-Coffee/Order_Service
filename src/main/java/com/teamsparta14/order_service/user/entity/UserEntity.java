package com.teamsparta14.order_service.user.entity;
import com.teamsparta14.order_service.domain.BaseEntity;
import com.teamsparta14.order_service.global.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name="member")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String userNickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
}
