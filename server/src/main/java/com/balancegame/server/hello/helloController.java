package com.balancegame.server.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class helloController {

    @RequestMapping("/home")
    public String index(){
        return "index";
    }
}
