package com.example.oauth2.oauth2.user

import com.example.oauth2.oauth2.exception.OAuth2AuthenticationProcessingException
import com.saysimple.decosk.security.users.GoogleOAuth2UserUnlink
import com.saysimple.decosk.security.users.KakaoOAuth2UserUnlink
import com.saysimple.decosk.security.users.NaverOAuth2UserUnlink
import com.saysimple.decosk.security.users.OAuth2Provider
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component

@RequiredArgsConstructor
@Component
class OAuth2UserUnlinkManager {
    private val googleOAuth2UserUnlink: GoogleOAuth2UserUnlink? = null
    private val kakaoOAuth2UserUnlink: KakaoOAuth2UserUnlink? = null
    private val naverOAuth2UserUnlink: NaverOAuth2UserUnlink? = null

    fun unlink(provider: OAuth2Provider, accessToken: String?) {
        if (OAuth2Provider.GOOGLE == provider) {
            googleOAuth2UserUnlink!!.unlink(accessToken)
        } else if (OAuth2Provider.NAVER == provider) {
            naverOAuth2UserUnlink!!.unlink(accessToken!!)
        } else if (OAuth2Provider.KAKAO == provider) {
            kakaoOAuth2UserUnlink!!.unlink(accessToken)
        } else {
            throw OAuth2AuthenticationProcessingException(
                ("Unlink with " + provider.getRegistrationId()).toString() + " is not supported"
            )
        }
    }
}
