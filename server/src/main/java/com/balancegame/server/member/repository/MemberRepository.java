package com.balancegame.server.member.repository;

import com.balancegame.server.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Boolean existsByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.oAuth2Id = :oauth2Id")
    Optional<Member> findByOAuth2Id(String oauth2Id);
}
