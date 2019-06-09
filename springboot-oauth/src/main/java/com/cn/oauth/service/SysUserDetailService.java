package com.cn.oauth.service;

import com.cn.oauth.modle.SysUserAuthentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


public class SysUserDetailService implements UserDetailsService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        //return new User(username, "{noop}123456", false, false, null);
        //User user = null;
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
            user.setPassword( new BCryptPasswordEncoder().encode("123456"));
            user.setAuthorities(list);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);

        }

        return user;//返回UserDetails的实现user不为空，则验证通过
    }
}
