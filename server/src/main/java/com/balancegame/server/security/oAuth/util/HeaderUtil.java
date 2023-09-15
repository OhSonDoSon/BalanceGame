package com.balancegame.server.security.oAuth.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class HeaderUtil {

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer";

    public static String getAccessToken(HttpServletRequest request){
        String headerValue = request.getHeader(AUTHORIZATION_HEADER);
        if(headerValue == null) return null;
        if(StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)){
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
