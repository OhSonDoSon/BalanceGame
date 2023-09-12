package com.balancegame.server.security.oAuth.service;

import com.balancegame.server.exception.CommonException;
import com.balancegame.server.exception.ErrorCode;
import com.balancegame.server.member.domain.Member;
import com.balancegame.server.member.repository.MemberRepository;
import com.balancegame.server.security.enums.AuthProvider;
import com.balancegame.server.security.enums.Role;
import com.balancegame.server.security.oAuth.dto.OAuth2UserInfo;
import com.balancegame.server.security.oAuth.dto.OAuth2UserInfoFactory;
import com.balancegame.server.security.oAuth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(oAuth2UserRequest);
        try {
            return this.processOAuth2User(oAuth2UserRequest, user);
        }catch (AuthenticationException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        if(!StringUtils.hasText(oAuth2UserInfo.getEmail())){
            throw new CommonException(ErrorCode.NO_EMAIL_EXIST);
        }

        Member member = memberRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        if(member == null){
            // 회원가입
            member = registerMember(authProvider, oAuth2UserInfo);
        }else{
            // 이미 가입되어 있는 경우
            if(!member.getAuthProvider().equals(authProvider)){
                throw new CommonException(ErrorCode.ALREADY_AUTH_EMAIL);
            }
            member = updateOauth2Info(member, oAuth2UserInfo);
        }
        return UserPrincipal.create(member, oAuth2UserInfo.getAttributes());
    }

    private Member registerMember(AuthProvider authProvider, OAuth2UserInfo oAuth2UserInfo){
        Member member = Member.builder()
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .authProvider(authProvider)
                .role(Role.ROLE_USER)
                .oAuth2Id(oAuth2UserInfo.getOAuth2Id())
                .build();

        return memberRepository.saveAndFlush(member);
    }

    private Member updateOauth2Info(Member member, OAuth2UserInfo oAuth2UserInfo){
        member.updateOAuthInfo(oAuth2UserInfo);
        return memberRepository.saveAndFlush(member);
    }
}
