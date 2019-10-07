package com.cn.oauth.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "order";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Since we want the protected resources to be accessible in the UI as well we need
        // session creation to be allowed (it's disabled by default in 2.0.6)
        //意义不大 认证服务器没必要在配置一个资源服务器配置  最好是直接使用 WebSecurityConfiguration
       http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/order/**").permitAll()
                .and()
                .anonymous()
                .and()
                //.csrf()
                //.requireCsrfProtectionMatcher(request -> "POST".equalsIgnoreCase(request.getMethod()))
                //.and()


        ;

    }
}