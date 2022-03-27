package com.cn.oauth.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String returnUrl = httpServletRequest.getParameter("returnUrl");
        if(null != returnUrl && !"".equals(returnUrl)){
            if(returnUrl.contains("?")){
                httpServletResponse.sendRedirect(returnUrl.concat("&token=").concat(authentication.getPrincipal().toString()));
            }else{
                httpServletResponse.sendRedirect(returnUrl.concat("?token=").concat(authentication.getPrincipal().toString()));
            }

        }else{
            httpServletResponse.sendRedirect("https://www.baidu.com");
        }
    }
}
