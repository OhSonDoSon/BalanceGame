package com.balancegame.server.security.oAuth.dto;

import java.util.Map;

public class NaverOAuth2User extends OAuth2UserInfo{

    public NaverOAuth2User(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getOAuth2Id() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if(response == null){
            return null;
        }
        return (String) response.get("id");
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if(response == null){
            return null;
        }
        return (String) response.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if(response == null){
            return null;
        }
        return (String) response.get("name");
    }
}
