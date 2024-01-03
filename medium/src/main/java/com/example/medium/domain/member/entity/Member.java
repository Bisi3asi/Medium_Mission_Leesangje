package com.example.medium.domain.member.entity;

import com.example.medium.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String profileMsg;

    @Setter
    @Column(nullable = false)
    private boolean isPrime;

    @Setter
    private LocalDateTime primeExpirationDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Role> authorities = new HashSet<>();

    @Setter
    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        return getGrantedAuthoritiesAsStrList().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getGrantedAuthoritiesAsStrList() {
        return authorities.stream()
                .map(Role::getValue)
                .toList();
    }
}
