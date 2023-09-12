package com.balancegame.server.security.config;

import com.balancegame.server.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${app.auth.tokenSecret}")
    private String secret;

    @Bean
    public JwtTokenProvider jwtTokenProvider(){
        return new JwtTokenProvider(secret);
    }
}
