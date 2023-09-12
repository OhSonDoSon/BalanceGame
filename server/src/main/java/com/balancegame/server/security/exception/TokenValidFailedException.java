package com.balancegame.server.security.exception;

import com.balancegame.server.exception.ErrorCode;

public class TokenValidFailedException extends RuntimeException{

    public TokenValidFailedException(){
        super(ErrorCode.NO_GENERATE_TOKEN.getErrorCode());
    }

    private TokenValidFailedException(String message){
        super(message);
    }
}
