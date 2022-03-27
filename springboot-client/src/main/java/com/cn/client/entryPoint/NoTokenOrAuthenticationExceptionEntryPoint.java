package com.cn.client.entryPoint;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoTokenOrAuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 没有token  或者 账号异常 需要重新的登录 重定向到登录页面
        System.out.printf("no token");
        response.getWriter().write("没有token");
    }
}
