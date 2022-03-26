package com.cn.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"authorizationRequest"})
@RequestMapping("/auth")
public class AuthPageController {


    @RequestMapping("/login")
    public ModelAndView requireAuthentication(@RequestParam String returnUrl) {
        ModelAndView modelAndView = new ModelAndView("/oauth/login");
        modelAndView.addObject("returnUrl",returnUrl);
        return modelAndView;
    }

    @RequestMapping("/approve")
    public String approve() {
        return "/oauth/approve";
    }

}
