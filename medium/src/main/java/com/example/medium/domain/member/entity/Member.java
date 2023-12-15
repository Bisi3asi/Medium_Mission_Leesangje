package com.example.medium.domain.member.entity;

import com.example.medium.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Entity
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // "ROLE_USER", "ROLE_ADMIN"
    @Column(nullable = false)
    private String authorities;

    public List<String> getAuthoritiesAsStrList(){
        return Collections.singletonList(authorities);
    }
}
