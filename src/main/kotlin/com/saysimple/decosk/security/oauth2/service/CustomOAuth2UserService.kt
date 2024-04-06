package com.saysimple.decosk.security.oauth2.service

import com.saysimple.decosk.security.oauth2.exception.OAuth2AuthenticationProcessingException
import com.saysimple.decosk.security.oauth2.user.OAuth2UserInfo
import com.saysimple.decosk.security.oauth2.user.OAuth2UserInfoFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

val log = KotlinLogging.logger {}

@RequiredArgsConstructor
@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        log.info { "processOAuth2User" }

        val registrationId = userRequest.clientRegistration
            .registrationId
        log.info { registrationId }


        val accessToken = userRequest.accessToken.tokenValue
        log.info { accessToken }

        log.info { oAuth2User.attributes }

        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            registrationId,
            accessToken,
            oAuth2User.attributes
        )
        log.info { oAuth2UserInfo }

        // OAuth2UserInfo field value validation
        if (!StringUtils.hasText(oAuth2UserInfo.email)) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        return OAuth2UserPrincipal(oAuth2UserInfo)
    }
}
