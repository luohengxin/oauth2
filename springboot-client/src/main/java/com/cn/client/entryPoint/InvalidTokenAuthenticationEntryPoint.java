package com.cn.client.entryPoint;

import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class InvalidTokenAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    private WebResponseExceptionTranslator<?> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // token过期 或错误 重定向到登录页面
        System.out.printf("token过期");
        response.getWriter().write("token过期");


    }

}
