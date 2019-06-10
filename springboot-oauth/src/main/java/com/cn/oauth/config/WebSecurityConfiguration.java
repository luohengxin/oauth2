package com.cn.oauth.config;


import com.cn.oauth.common.PermitAllUrl;
import com.cn.oauth.provider.EmailCodeProvider;
import com.cn.oauth.service.SysClientDetailService;
import com.cn.oauth.service.SysUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;


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



    /**
     * 用户验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //表单登录,loginPage为登录请求的url,loginProcessingUrl为表单登录处理的URL
                .formLogin().loginPage("/auth/login").loginProcessingUrl("/auth/authorize")
                .and()
                //允许访问
                .requestMatchers().antMatchers("/auth/login","/auth/authorize","/auth/approve").anyRequest()
                .and()
                .authorizeRequests().antMatchers(PermitAllUrl.permitAllUrl("/auth/login","/auth/authorize","/auth/approve")).permitAll()
                .anyRequest().authenticated()
                //禁用跨站伪造
                .and()
                .csrf().disable();
        http.authenticationProvider(emailCodeProvider());
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
