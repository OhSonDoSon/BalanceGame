package com.balancegame.server.security.oAuth.handler;

import com.balancegame.server.exception.CommonException;
import com.balancegame.server.exception.ErrorCode;
import com.balancegame.server.member.domain.MemberRefreshToken;
import com.balancegame.server.member.repository.MemberRefreshTokenRepository;
import com.balancegame.server.security.config.AppProperties;
import com.balancegame.server.security.enums.AuthProvider;
import com.balancegame.server.security.enums.Role;
import com.balancegame.server.security.jwt.JwtToken;
import com.balancegame.server.security.jwt.JwtTokenProvider;
import com.balancegame.server.security.oAuth.dto.OAuth2UserInfo;
import com.balancegame.server.security.oAuth.dto.OAuth2UserInfoFactory;
import com.balancegame.server.security.oAuth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.balancegame.server.security.oAuth.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static com.balancegame.server.security.oAuth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.balancegame.server.security.oAuth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final MemberRefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if(response.isCommitted()){
            logger.debug("Response has already been committed.");
            return;
        }
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response){
        super.clearAuthenticationAttributes(request);
        oAuth2AuthorizationRequestBasedOnCookieRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())){
            throw new CommonException(ErrorCode.INVALID_REDIRECT_URI);
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        AuthProvider authProvider = AuthProvider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = (OidcUser) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser)authentication.getPrincipal()).getAuthorities();

        Role role = hasAuthority(authorities, Role.ROLE_ADMIN.getRoleCode()) ? Role.ROLE_ADMIN : Role.ROLE_USER;

        Date now = new Date();
        JwtToken accessToken = jwtTokenProvider.createJwtToken(
                userInfo.getOAuth2Id()
                ,role.getRoleCode()
                ,new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpireTime())
        );

        long refreshTokenExpireTime = appProperties.getAuth().getRefreshTokenExpireTime();
        JwtToken refreshToken = jwtTokenProvider.createJwtToken(
                appProperties.getAuth().getTokenSecret()
                , new Date(now.getTime() + refreshTokenExpireTime)
        );

        //DB 저장
        MemberRefreshToken memberRefreshToken = refreshTokenRepository.findByOAuth2Id(userInfo.getOAuth2Id()).orElse(null);
        if(memberRefreshToken == null){
            memberRefreshToken = MemberRefreshToken.builder()
                    .oAuth2Id(userInfo.getOAuth2Id())
                    .refreshToken(refreshToken.getToken())
                    .build();
            refreshTokenRepository.saveAndFlush(memberRefreshToken);
        }else{
            memberRefreshToken.updateRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) (refreshTokenExpireTime / 60);

        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .build().toUriString();
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority){
        if(authorities == null)return false;
        for(GrantedAuthority grantedAuthority : authorities){
            if(authority.equals(grantedAuthority.getAuthority())) return true;
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri){
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }
}
