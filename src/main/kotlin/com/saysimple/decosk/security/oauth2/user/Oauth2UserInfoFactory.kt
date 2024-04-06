package com.saysimple.decosk.security.oauth2.user

import com.saysimple.decosk.security.oauth2.exception.OAuth2AuthenticationProcessingException


object OAuth2UserInfoFactory {
    fun getOAuth2UserInfo(
        registrationId: String,
        accessToken: String,
        attributes: Map<String, Any>
    ): OAuth2UserInfo {
        return when (registrationId) {
            OAuth2Provider.GOOGLE.registrationId -> GoogleOAuth2UserInfo(accessToken, attributes)
            OAuth2Provider.NAVER.registrationId -> NaverOAuth2UserInfo(accessToken, attributes)
            OAuth2Provider.KAKAO.registrationId -> KakaoOAuth2UserInfo(accessToken, attributes)
            else -> throw OAuth2AuthenticationProcessingException("Login with $registrationId is not supported")
        }
    }
}
