package com.cn.oauth.service;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
//用于保证只有一个HttpSecurity
public interface WebSecurityConfigurer {

    void configure(AuthenticationManagerBuilder auth) throws Exception;

    void configure(HttpSecurity http) throws Exception;

    void configure(WebSecurity http) throws Exception;


}
