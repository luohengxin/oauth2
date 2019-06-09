package com.cn.oauth.provider;

import com.cn.oauth.modle.SysUserAuthentication;
import com.cn.oauth.token.EmailCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class EmailCodeProvider implements AuthenticationProvider {


    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();


    private PasswordEncoder passwordEncoder;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();

        SysUserAuthentication user = null;
        if("admin".equals(username)) {
            //IntegrationAuthentication auth = IntegrationAuthenticationContext.get();
            //这里可以通过auth 获取 user 值
            //然后根据当前登录方式type 然后创建一个sysuserauthentication 重新设置 username 和 password
            //比如使用手机验证码登录的， username就是手机号 password就是6位的验证码{noop}000000
            //System.out.println(auth);
            List<GrantedAuthority> list = AuthorityUtils.createAuthorityList("admin_role","orderById"); //所谓的角色，只是增加ROLE_前缀
            user = new SysUserAuthentication();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setAuthorities(list);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

        }

        //验证  1、是否锁定，过期
        //     2、 密码
        CheckUserPre(user);
        CheckUser(user,authentication);
        EmailCodeAuthenticationToken result = new EmailCodeAuthenticationToken(user, authentication.getCredentials(), authoritiesMapper.mapAuthorities(user.getAuthorities()));
        result.setDetails(authentication.getDetails());
        return result;
    }

    private void CheckUserPre(SysUserAuthentication user) {
    }

    private void CheckUser(SysUserAuthentication user,Authentication authentication) {
        //验证时间等有没有过期
        if(!passwordEncoder.matches((String)authentication.getCredentials(),user.getPassword())){
            throw new RuntimeException("密码不正确");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailCodeAuthenticationToken.class.isAssignableFrom(authentication) ;
    }
}
