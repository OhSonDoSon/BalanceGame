package com.balancegame.server.security.exception;

public class OAuthProviderMissMatchException extends RuntimeException{

    public OAuthProviderMissMatchException(String message) {
        super(message);
    }
}
