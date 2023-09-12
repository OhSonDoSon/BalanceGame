package com.balancegame.server.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {

    ROLE_USER("ROLE_USER", "사용자")
    ,ROLE_ADMIN("ROLE_ADMIN","관리자")
    ,ROLE_GUEST("ROLE_GUEST", "게스트")
    ;

    private final String roleCode;
    private final String roleName;

    public static Role of(String roleCode){
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleCode().equals(roleCode))
                .findAny()
                .orElse(ROLE_GUEST);
    }
}
