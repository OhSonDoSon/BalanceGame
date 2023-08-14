package com.balancegame.server.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ExceptionHandler(value = CommonException.class)
    public ResponseEntity<?> commonExceptionHandler(CommonException e){
        logger.error(e.getErrorCode().getErrorMessage() + " : " + e.getErrorCode().getErrorCode());
        HttpStatus httpStatus = e.getErrorCode().getHttpStatus();
        ErrorMessage errorMessage = new ErrorMessage(e.getErrorCode().getErrorCode(), e.getErrorCode().getErrorMessage());
        return ResponseEntity.status(httpStatus).body(errorMessage);
    }
}
