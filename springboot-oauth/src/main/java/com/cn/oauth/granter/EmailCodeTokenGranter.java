package com.cn.oauth.granter;

import com.cn.oauth.token.EmailCodeAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

public class EmailCodeTokenGranter extends AbstractTokenGranter {

    private static final String GRANT_TYPE = "email_code";

    private AuthenticationManager authenticationManager;

    public EmailCodeTokenGranter(AuthenticationManager authenticationManager,AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String email = parameters.get("email");  //客户端提交的用户名
        String password = parameters.get("password");  //客户端提交的验证码

/*
        // 从库里查用户
        SysUserAuthentication user = null;
        if("admin".equals(email)) {
            //IntegrationAuthentication auth = IntegrationAuthenticationContext.get();
            //这里可以通过auth 获取 user 值
            //然后根据当前登录方式type 然后创建一个sysuserauthentication 重新设置 username 和 password
            //比如使用手机验证码登录的， username就是手机号 password就是6位的验证码{noop}000000
            //System.out.println(auth);
            List<GrantedAuthority> list = AuthorityUtils.createAuthorityList("admin_role","orderById"); //所谓的角色，只是增加ROLE_前缀
            user = new SysUserAuthentication();
            user.setUsername(email);
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setAuthorities(list);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

        }
        if(user == null) {
            throw new InvalidGrantException("用户不存在");
        }
*/


        Authentication userAuth = new EmailCodeAuthenticationToken(email, password);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        userAuth = authenticationManager.authenticate(userAuth);
        // 关于user.getAuthorities(): 我们的自定义用户实体是实现了
        // org.springframework.security.core.userdetails.UserDetails 接口的, 所以有 user.getAuthorities()
        // 当然该参数传null也行
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
