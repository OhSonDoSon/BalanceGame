package com.balancegame.server.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private final String message;
    private final T data;

    public CommonResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
