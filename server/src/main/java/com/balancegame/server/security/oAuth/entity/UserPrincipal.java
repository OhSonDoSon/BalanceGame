package com.balancegame.server.security.oAuth.entity;

import com.balancegame.server.member.domain.Member;
import com.balancegame.server.security.enums.AuthProvider;
import com.balancegame.server.security.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserPrincipal implements OAuth2User, UserDetails, OidcUser {

    private final String oAuth2Id;
    private final String email;
    private final Role role;
    private final AuthProvider authProvider;
    private final Collection<GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> attributes;

    public static UserPrincipal create(Member member){
        return new UserPrincipal(
                member.getOAuth2Id()
                , member.getEmail()
                , Role.ROLE_USER
                , member.getAuthProvider()
                ,Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_USER.getRoleCode()))
        );
    }

    public static UserPrincipal create(Member member, Map<String, Object> attributes){
        UserPrincipal userPrincipal = create(member);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2Id;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}
