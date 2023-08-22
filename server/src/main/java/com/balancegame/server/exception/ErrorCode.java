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
    ,NO_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "SECURITY_005","권한 정보가 없는 토큰입니다.")
    ,INVALID_PROV_TYPE(HttpStatus.BAD_REQUEST, "SECURITY_006","유효하지 않은 제공자입니다.")
    ,NO_EMAIL_EXIST(HttpStatus.BAD_REQUEST, "SECURITY_007","해당 OAuth2 제공자로부터 이메일을 가져오지 못하였습니다.")
    ,ALREADY_AUTH_EMAIL(HttpStatus.BAD_REQUEST, "SECURITY_008","이미 해당 플랫폼으로 회원가입 이력이 있습니다.")
    ,INVALID_REDIRECT_URI(HttpStatus.BAD_REQUEST, "SECURITY_009","리아이렉트 URI 가 옳지 않습니다.")
    //Member
    ,NO_EXISTS_MEMBER(HttpStatus.BAD_REQUEST,"MEMBER_001", "올바른 회원이 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
}
