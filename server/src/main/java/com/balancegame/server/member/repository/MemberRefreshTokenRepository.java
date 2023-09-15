package com.balancegame.server.member.repository;

import com.balancegame.server.member.domain.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {

    @Query("SELECT rt FROM MemberRefreshToken rt WHERE rt.oAuth2Id = :oauth2Id")
    Optional<MemberRefreshToken> findByOAuth2Id(@Param("oauth2Id") String oauth2Id);

    @Query("SELECT rt FROM MemberRefreshToken rt WHERE rt.oAuth2Id = :oauth2Id and rt.refreshToken = :refreshToken")
    Optional<MemberRefreshToken> findByOAuth2IdAndRefreshToken(String oAuth2Id, String refreshToken);
}
