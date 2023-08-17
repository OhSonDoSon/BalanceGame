package com.balancegame.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //Security
    NOT_CORRECT_TOKEN(HttpStatus.BAD_REQUEST, "SECURITY_001","잘못된 JWT 서명입니다.")
    ,EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "SECURITY_002","만료된 JWT 토큰입니다.")
    ,NOT_SUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "SECURITY_003","지원되지 않는 JWT 토큰입니다.")
    ,NOT_VALID_TOKEN(HttpStatus.BAD_REQUEST, "SECURITY_004","유효하지 않은 JWT 토큰입니다.")
    //Member
    ,NO_EXISTS_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER_001", "올바른 회원이 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
