package com.balancegame.server.member.domain;

import com.balancegame.server.common.domain.BaseDateEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRefreshToken extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(name = "o_auth2_id", nullable = false)
    private String oAuth2Id;

    @Column(nullable = false)
    private String refreshToken;

    @Builder
    public MemberRefreshToken(String oAuth2Id, String refreshToken) {
        this.oAuth2Id = oAuth2Id;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
