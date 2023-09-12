package com.balancegame.server.security.oAuth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class NaverOAuth2Service {

    private String authorizationUri;


    public String generateRedirectUri(){
        return "";
    }

    private String generateState()
    {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
