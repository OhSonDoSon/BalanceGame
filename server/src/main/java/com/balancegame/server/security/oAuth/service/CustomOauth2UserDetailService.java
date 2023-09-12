package com.balancegame.server.security.oAuth.service;

import com.balancegame.server.exception.CommonException;
import com.balancegame.server.exception.ErrorCode;
import com.balancegame.server.member.domain.Member;
import com.balancegame.server.member.repository.MemberRepository;
import com.balancegame.server.security.oAuth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorCode.NO_EXISTS_MEMBER.getErrorCode()));
        return UserPrincipal.create(member);
    }
}
