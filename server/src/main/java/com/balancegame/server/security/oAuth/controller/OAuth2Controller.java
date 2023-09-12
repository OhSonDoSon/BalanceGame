package com.balancegame.server.security.oAuth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {


    @PostMapping("/naver")
    public String naverOAuth2Connect(){
        return null;
    }

    @GetMapping("/callback/naver")
    public String callback(){
        System.out.println("complete");
        return "index";
    }


}
