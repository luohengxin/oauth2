package com.cn.oauth.config;


import com.cn.oauth.common.PermitAllUrl;
import com.cn.oauth.handler.AuthSuccessHandler;
import com.cn.oauth.provider.EmailCodeProvider;
import com.cn.oauth.service.SysClientDetailService;
import com.cn.oauth.service.SysUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;


@Configuration
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{



    @Bean
    public ClientDetailsService clientDetailsService(){
        return new SysClientDetailService();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return new SysUserDetailService();
    }

    private EmailCodeProvider emailCodeProvider(){
        EmailCodeProvider emailCodeProvider = new  EmailCodeProvider();
        emailCodeProvider.setPasswordEncoder(passwordEncoder());
        return emailCodeProvider;
    }

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider(){
        AuthenticationUserDetailsService authenticationUserDetailsService = new UserDetailsByNameServiceWrapper(userDetailsService);
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(authenticationUserDetailsService);
        return preAuthenticatedAuthenticationProvider;
    }



    /**
     * 用户验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);//不能与下面的同时使用 否则无效
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(emailCodeProvider());
        auth.authenticationProvider(preAuthenticatedAuthenticationProvider()); //refresh_token 会使用
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //表单登录,loginPage为登录请求的url,loginProcessingUrl为表单登录处理的URL
                .formLogin().loginPage("/auth/login.html").loginProcessingUrl("/login.do")
                //.successHandler(authSuccessHandler) //登录成功后的处理handler
                .and()
                .authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl("/auth/login.html","/login.do","/auth/approve")).permitAll()
                .antMatchers("/order/**").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
        ;
    }

    /**
     * Spring Boot 2 配置，这里要bean 注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }



}
