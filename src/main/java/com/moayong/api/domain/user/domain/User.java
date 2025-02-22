package com.moayong.api.domain.user.domain;

import com.moayong.api.domain.user.enums.AuthProvider;
import com.moayong.api.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    private String providerId;

}
