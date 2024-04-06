package com.saysimple.decosk.security.oauth2.handler


import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.MODE_PARAM_COOKIE_NAME
import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import com.saysimple.decosk.security.oauth2.service.OAuth2UserPrincipal
import com.saysimple.decosk.security.oauth2.user.OAuth2Provider
import com.saysimple.decosk.security.oauth2.user.OAuth2UserUnlinkManager
import com.saysimple.decosk.security.oauth2.utils.CookieUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

val log = KotlinLogging.logger {}

@RequiredArgsConstructor
@Component
class OAuth2AuthenticationSuccessHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val oAuth2UserUnlinkManager: OAuth2UserUnlinkManager
) : SimpleUrlAuthenticationSuccessHandler() {


    @Throws(IOException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest, response: HttpServletResponse,
        authentication: Authentication
    ) {
        log.info { "onAuthenticationSuccess" }

        val targetUrl: String = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest, response: HttpServletResponse,
        authentication: Authentication
    ): String {
        log.info { "determineTargetUrl" }

        val redirectUri: String? =
            CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value
        val targetUrl = redirectUri ?: (defaultTargetUrl)
        val mode: String? = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)?.value

        val principal: OAuth2UserPrincipal = getOAuth2UserPrincipal(authentication)
            ?: return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString()

        log.info { "$redirectUri, $targetUrl, $mode, $principal" }

        if ("login".equals(mode, ignoreCase = true)) {
            // TODO: DB 저장
            // TODO: 액세스 토큰, 리프레시 토큰 발급
            // TODO: 리프레시 토큰 DB 저장
            log.info {
                "${"email={}, name={}, nickname={}, accessToken={}"} ${
                    arrayOf<Any?>(
                        principal.getUserInfo().email,
                        principal.getUserInfo().name,
                        principal.getUserInfo().nickname,
                        principal.getUserInfo().accessToken
                    )
                }"
            }

            val accessToken: String = "test_access_token"
            val refreshToken: String = "test_refresh_token"

            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .build().toUriString()

        } else if ("unlink".equals(mode, ignoreCase = true)) {

            val accessToken: String? = principal.getUserInfo().accessToken
            val provider: OAuth2Provider? = principal.getUserInfo().provider

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

    private fun getOAuth2UserPrincipal(authentication: Authentication): OAuth2UserPrincipal? {
        log.info { "getOAuth2UserPrincipal" }
        val principal: Any = authentication.principal

        if (principal is OAuth2UserPrincipal) {
            return principal
        }
        return null
    }

    protected fun clearAuthenticationAttributes(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        log.info { "getOAuth2UserPrincipal" }
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }
}
