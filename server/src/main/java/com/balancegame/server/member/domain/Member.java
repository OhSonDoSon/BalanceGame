package com.balancegame.server.member.domain;

import com.balancegame.server.common.domain.BaseDateEntity;
import com.balancegame.server.security.enums.AuthProvider;
import com.balancegame.server.security.enums.Role;
import com.balancegame.server.security.oAuth.dto.response.OAuth2UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "o_auth2_id")
    private String oAuth2Id;

    @Builder
    public Member(Long memberId, String name, String email, String password, AuthProvider authProvider, Role role, String oAuth2Id) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authProvider = authProvider;
        this.role = role;
        this.oAuth2Id = oAuth2Id;
    }

    public void updateOAuthInfo(OAuth2UserInfo oAuth2UserInfo){
        this.oAuth2Id = oAuth2UserInfo.getOAuth2Id();
        this.name = oAuth2UserInfo.getName();
    }

    public Member registerMember(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo){
        return Member.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .authProvider(authProvider)
                .role(Role.ROLE_USER)
                .oAuth2Id(oAuth2UserInfo.getOAuth2Id())
                .build();
    }

}

