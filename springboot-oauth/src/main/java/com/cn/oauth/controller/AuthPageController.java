package com.cn.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"authorizationRequest"})
@RequestMapping("/auth")
public class AuthPageController {


    @RequestMapping("/login")
    public String requireAuthentication() {
        return "/oauth/login";
    }

    @RequestMapping("/approve")
    public String approve() {
        return "/oauth/approve";
    }

}
