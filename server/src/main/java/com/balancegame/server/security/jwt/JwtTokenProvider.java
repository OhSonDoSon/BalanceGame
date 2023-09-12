package com.balancegame.server.security.jwt;

import com.balancegame.server.exception.CommonException;
import com.balancegame.server.exception.ErrorCode;
import com.balancegame.server.security.exception.TokenValidFailedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "role";
    private final Key key;

    public JwtTokenProvider(String secret){
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public JwtToken createJwtToken(String id, Date expiry){
        return new JwtToken(id, expiry, key);
    }

    public JwtToken createJwtToken(String id, String role, Date expiry){
        return new JwtToken(id, role, expiry, key);
    }

    public JwtToken convertJwtToken(String token){
        return new JwtToken(token, key);
    }

    public Authentication getAuthentication(JwtToken jwtToken){

        if(jwtToken.validate()){
            Claims claims = jwtToken.getTokenClaims();
            if(claims.isEmpty()){
                throw new TokenValidFailedException();
            }
            //클레임에서 권한 정보 가져오기
            Collection<? extends GrantedAuthority> authorities =
                    Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                            .map(SimpleGrantedAuthority::new)
                            .toList();
            // User 객체를 만들어서 Authentication 리턴
            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, jwtToken, authorities);
        }else{
            throw new TokenValidFailedException();
        }
    }
}
