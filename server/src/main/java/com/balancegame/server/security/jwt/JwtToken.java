package com.balancegame.server.security.jwt;

import com.balancegame.server.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtToken {

    @Getter
    private final String token;
    private final Key key;

    private static final String AUTHORITIES_KEY = "role";

    JwtToken(String id, Date expiry, Key key){
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    JwtToken(String id, String role, Date expiry, Key key){
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry){
        return Jwts.builder()
                .setSubject(id)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    private String createAuthToken(String id, String role, Date expiry){
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, role)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiry)
                .compact();
    }

    public boolean validate(){
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims(){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (SecurityException | MalformedJwtException e){
            log.info(ErrorCode.NOT_CORRECT_TOKEN.getErrorCode());
        } catch (ExpiredJwtException e){
            log.info(ErrorCode.EXPIRED_TOKEN.getErrorCode());
        } catch (UnsupportedJwtException e){
            log.info(ErrorCode.NOT_SUPPORTED_TOKEN.getErrorCode());
        } catch (IllegalArgumentException e){
            log.info(ErrorCode.NOT_VALID_TOKEN.getErrorCode());
        }
        return null;
    }

    public Claims getExpiredTokenClaims(){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (ExpiredJwtException e){
            log.info(ErrorCode.EXPIRED_TOKEN.getErrorCode());
        }
        return null;
    }
}
