package com.cn.oauth.service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Date;

public class RedisTokenServices extends DefaultTokenServices {

    private TokenStore tokenStore;


    public void setTokenStore(TokenStore tokenStore) {
        super.setTokenStore(tokenStore);
        this.tokenStore =  tokenStore;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication authentication = super.loadAuthentication(accessTokenValue);
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(authentication);
        OAuth2RefreshToken refreshToken = updateRefreshToken(authentication,accessToken);
        updateAccessToken(authentication, accessToken,refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        refreshToken = accessToken.getRefreshToken();
        if (refreshToken != null) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }

        return authentication;
    }

    private void updateAccessToken(OAuth2Authentication authentication ,OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {
        if(accessToken instanceof DefaultOAuth2AccessToken){
            DefaultOAuth2AccessToken defaultOAuth2AccessToken = (DefaultOAuth2AccessToken) accessToken;
            int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
            if (validitySeconds > 0) {
                defaultOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
            }
            defaultOAuth2AccessToken.setRefreshToken(refreshToken);
        }
    }

    private OAuth2RefreshToken updateRefreshToken(OAuth2Authentication authentication,OAuth2AccessToken accessToken) {
        if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
            return null;
        }
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        if(refreshToken instanceof ExpiringOAuth2RefreshToken){
            ExpiringOAuth2RefreshToken expiringOAuth2RefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
            String value = expiringOAuth2RefreshToken.getValue();
            int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
            if (validitySeconds > 0) {
                return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
                        + (validitySeconds * 1000L)));
            }
        }

        return refreshToken;
    }
}
