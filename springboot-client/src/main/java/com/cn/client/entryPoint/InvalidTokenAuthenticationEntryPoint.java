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
        System.out.println("无效的token");
        System.out.println(authException.getCause());
        //续签

        //解析异常，如果是401则处理
        ResponseEntity<?> result = null;
        try {
            result = exceptionTranslator.translate(authException);
        } catch (Exception e) {
            e.printStackTrace();
            super.commence(request, response, authException);
        }
        if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
            formData.add("client_id", "client");
            formData.add("client_secret", "123456");
            formData.add("grant_type", "refresh_token");

            String refreshToken = (String) request.getSession().getAttribute("refresh_token");
            formData.add("refresh_token", refreshToken);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            Map map = null;
            try {
                map = restTemplate.exchange("http://localhost:8080/oauth/token", HttpMethod.POST,
                        new HttpEntity<MultiValueMap<String, String>>(formData, headers), Map.class).getBody();
            } catch (RestClientException e) {
                super.commence(request, response, authException);
            }
            //如果刷新异常,则坐进一步处理
            if (map.get("error") != null) {
                // 返回指定格式的错误信息
                response.setStatus(401);
                response.setHeader("Content-Type", "application/json;charset=utf-8");
                response.getWriter().print("{\"code\":1,\"message\":\"" + map.get("error_description") + "\"}");
                response.getWriter().flush();
                //如果是网页,跳转到登陆页面
                //response.sendRedirect("login");
            } else {
                //如果刷新成功则存储cookie并且跳转到原来需要访问的页面
                for (Object key : map.keySet()) {
                    request.getSession().setAttribute(key.toString(), map.get(key).toString());
                }
                response.addCookie(new Cookie("access_token", map.get("access_token").toString()));//前端获取
                request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
            }
        } else {
            //如果不是401异常，则以默认的方法继续处理其他异常
            super.commence(request, response, authException);
        }

    }

}
