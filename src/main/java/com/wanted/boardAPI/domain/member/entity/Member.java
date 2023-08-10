package com.wanted.boardAPI.domain.member.entity;

import com.wanted.boardAPI.base.baseEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    public Map<String, Object> toClaims() {
        return Map.of(
                "id", getId(),
                "email", getEmail()
        );
    }
}
