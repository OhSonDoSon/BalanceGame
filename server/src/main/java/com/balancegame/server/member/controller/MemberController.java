package com.balancegame.server.member.controller;

import com.balancegame.server.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping
    public ResponseEntity<ApiResponse<Void>> showUserInfo(@AuthenticationPrincipal User user){
        String oauth2Id = user.getUsername();
        return new ResponseEntity<>(new ApiResponse<>(oauth2Id, null), HttpStatus.OK);
    }
}
