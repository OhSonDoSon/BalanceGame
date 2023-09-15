package com.balancegame.server.security.oAuth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {
/*
 refresh_token의 경우 프론트엔드에서 조작할 수 없도록 httpOnly 설정을 넣어주었고,
 access_token은 로그아웃 과정 시, 프론트엔드에서 쿠키에서 토큰을 지워주는 작업을 할 수 있도록 httpOnly 설정을 뺐습니다.
 또한, www가 붙고 안붙고를 다른 도메인으로 인식하는 문제를 해결하기 위해 쿠키의 도메인을 일괄적으로 지정받을 수 있도록 setDomain을 사용하여 맞춰주었고, 이에 따라 로그아웃 과정도 프론트에서 쿠키를 지우는 것이 아닌 백엔드에서 쿠키 시간을 0로 설정하여 새로 만들 수 있도록 로직을 바꿔주었습니다.
*/

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<String> readServletCookie(HttpServletRequest request, String name){
        return Arrays.stream(request.getCookies())
                .filter(cookie -> name.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // HttpOnly 권한을 풀고 access 토큰을 저장하기 위함
    public static void addCookieForAccess(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object){
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls){
        return cls.cast(SerializationUtils
                .deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
