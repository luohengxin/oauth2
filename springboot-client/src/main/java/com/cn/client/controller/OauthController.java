package com.cn.client.controller;


import com.cn.client.modle.OauthRequestBody;
import com.cn.client.modle.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

//@Controller
public class OauthController {


    @Value("${oauth.url}")
    private String oauthUrl;

    @GetMapping("/login.html")
    public String login() {
        return "/login";
    }

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostMapping("/login.do")
    public String login(@RequestBody OauthRequestBody requestBody) {

        requestBody.setClient_id("client");
        requestBody.setClient_secret("123456");
        TokenModel tokenModel = restTemplate.postForObject(oauthUrl, requestBody, TokenModel.class);
        // 登录失败 者需要转到重新登录页面
        return requestBody.getRedirect_uri() + "?access_token=" + tokenModel.getAccess_token();
    }

}
