package com.example.oauth2.oauth2.handler

import com.example.oauth2.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.example.oauth2.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME
import com.example.oauth2.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
import com.example.oauth2.oauth2.service.OAuth2UserPrincipal
import com.example.oauth2.oauth2.user.OAuth2Provider
import com.example.oauth2.oauth2.user.OAuth2UserUnlinkManager
import com.example.oauth2.oauth2.util.CookieUtils
import lombok.RequiredArgsConstructor
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

@lombok.extern.slf4j.Slf4j
@RequiredArgsConstructor
@org.springframework.stereotype.Component
class OAuth2AuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository? = null
    private val oAuth2UserUnlinkManager: OAuth2UserUnlinkManager? = null

    @kotlin.Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: jakarta.servlet.http.HttpServletRequest, response: jakarta.servlet.http.HttpServletResponse,
        authentication: org.springframework.security.core.Authentication
    ) {

        val targetUrl: String

        targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: jakarta.servlet.http.HttpServletRequest, response: jakarta.servlet.http.HttpServletResponse,
        authentication: org.springframework.security.core.Authentication
    ): String {

        val redirectUri: java.util.Optional<String> =
            CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map { obj: jakarta.servlet.http.Cookie -> obj.value }

        val targetUrl = redirectUri.orElse(defaultTargetUrl)

        val mode: String = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map { obj: jakarta.servlet.http.Cookie -> obj.value }
            .orElse("")

        val principal: OAuth2UserPrincipal? = getOAuth2UserPrincipal(authentication)

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString()
        }

        if ("login".equals(mode, ignoreCase = true)) {
            // TODO: DB 저장
            // TODO: 액세스 토큰, 리프레시 토큰 발급
            // TODO: 리프레시 토큰 DB 저장
            OAuth2AuthenticationSuccessHandler.log.info(
                "email={}, name={}, nickname={}, accessToken={}", principal.getUserInfo().getEmail(),
                principal.getUserInfo().getName(),
                principal.getUserInfo().getNickname(),
                principal.getUserInfo().getAccessToken()
            )

            val accessToken: String = "test_access_token"
            val refreshToken: String = "test_refresh_token"

            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString()

        } else if ("unlink".equals(mode, ignoreCase = true)) {

            val accessToken: String = principal.getUserInfo().getAccessToken()
            val provider: OAuth2Provider = principal.getUserInfo().getProvider()

            // TODO: DB 삭제
            // TODO: 리프레시 토큰 삭제
            oAuth2UserUnlinkManager.unlink(provider, accessToken)

            return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString()
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", "Login failed")
            .build().toUriString()
    }

    private fun getOAuth2UserPrincipal(authentication: org.springframework.security.core.Authentication): OAuth2UserPrincipal? {
        val principal: Any = authentication.principal

        if (principal is OAuth2UserPrincipal) {
            return principal as OAuth2UserPrincipal
        }
        return null
    }

    protected fun clearAuthenticationAttributes(
        request: jakarta.servlet.http.HttpServletRequest?,
        response: jakarta.servlet.http.HttpServletResponse?
    ) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }
}
