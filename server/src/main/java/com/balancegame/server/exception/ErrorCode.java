package com.balancegame.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_EXISTS_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER_001", "올바른 회원이 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
