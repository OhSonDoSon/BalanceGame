package com.balancegame.server.security.config;

import com.balancegame.server.member.repository.MemberRefreshTokenRepository;
import com.balancegame.server.security.exception.JwtAuthenticationEntryPoint;
import com.balancegame.server.security.jwt.JwtTokenProvider;
import com.balancegame.server.security.jwt.filter.JwtAuthenticationFilter;
import com.balancegame.server.security.oAuth.handler.OAuth2AuthenticationFailureHandler;
import com.balancegame.server.security.oAuth.handler.OAuth2AuthenticationSuccessHandler;
import com.balancegame.server.security.oAuth.handler.TokenAccessDeniedHandler;
import com.balancegame.server.security.oAuth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.balancegame.server.security.oAuth.service.CustomOauth2UserDetailService;
import com.balancegame.server.security.oAuth.service.CustomOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomOauth2UserService customOauth2UserService;
    private final CustomOauth2UserDetailService customOauth2UserDetailService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final MemberRefreshTokenRepository refreshTokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                //TODO 로그인 후, 리다이렉트 페이지
//                .formLogin(form -> form.loginPage("/home").permitAll())
                .rememberMe(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(tokenAccessDeniedHandler));

        //요청에 대한 권한 설정
        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/api/oauth2/**").permitAll().anyRequest().authenticated());


        http.oauth2Login(oauth ->
                oauth.authorizationEndpoint(point -> point.baseUri("/api/oauth2/authorization")
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .redirectionEndpoint(point -> point.baseUri("/api/oauth2/callback/*")).permitAll()
                        .userInfoEndpoint(point -> point.userService(customOauth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                        .failureHandler(oAuth2AuthenticationFailureHandler())
        );

//        http.logout(logout ->
//                logout.clearAuthentication(true)
//                        .deleteCookies("JSESSIONID")
//        );

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.getOrBuild();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter tokenAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /*
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 성공 핸들러
     * */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
                jwtTokenProvider,
                appProperties,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    @Bean
        public UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

}
