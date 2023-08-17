package com.balancegame.server.member.domain;

import com.balancegame.server.security.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;
    private String password;
    private String type;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

//    @Enumerated(EnumType.STRING)
//    private Role role;
}

