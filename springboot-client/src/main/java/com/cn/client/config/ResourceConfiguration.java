package com.cn.client.config;


import com.cn.client.entryPoint.InvalidTokenAuthenticationEntryPoint;
import com.cn.client.entryPoint.NoTokenAuthenticationEntryPoint;
import com.cn.client.handler.UserAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceConfiguration extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "order";


    @Bean
    public ResourceServerTokenServices tokenService(){
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId("client");
        remoteTokenServices.setClientSecret("123456");
        remoteTokenServices.setTokenName("token");
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
        return remoteTokenServices;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository(){
        return new HttpSessionCsrfTokenRepository();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID)
                 .stateless(true)
                 .tokenServices(tokenService())
                 .accessDeniedHandler(new UserAccessDeniedHandler())
                 .authenticationEntryPoint(new InvalidTokenAuthenticationEntryPoint())
                ;

    }



    @Override
    public void configure(HttpSecurity http) throws Exception {
        //super.configure(http); //不能调用该方法 会导致后面认证规则失效
        // Since we want the protected resources to be accessible in the UI as well we need
        // session creation to be allowed (it's disabled by default in 2.0.6)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                //.anyRequest().authenticated() //不能放在前
                .antMatchers("/order/**").hasRole("USER")
                .antMatchers("/order2/**").hasRole("ADMIN")
                .antMatchers("/order3/**").hasAuthority("orderById")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                //.accessDeniedHandler() //会覆盖resources中设置的handler
                .authenticationEntryPoint(new NoTokenAuthenticationEntryPoint())//可做token续签
                .and()
                //.addFilterBefore()//在制定位置插入过滤器 特殊场景会使用
                .headers() //设置http 头相关参数
                .cacheControl().and()
                .xssProtection().xssProtectionEnabled(true).and()
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(request -> "POST".equalsIgnoreCase(request.getMethod()))
                .csrfTokenRepository(csrfTokenRepository())




;
    }
}
