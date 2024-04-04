package com.saysimple.decosk.security.handlers

import com.saysimple.decosk.security.HttpCookieOAuth2AuthorizationRequestRepository
import com.saysimple.decosk.security.services.OAuth2UserPrincipal
import com.saysimple.decosk.security.users.OAuth2UserUnlinkManager
import com.saysimple.decosk.security.utils.CookieUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

private val log = KotlinLogging.logger {}


@Slf4j
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

        val targetUrl: String = determineTargetUrl(request, response, authentication)

        if (response.isCommitted) {
            log.debug { "Response has already been committed. Unable to redirect to $targetUrl" }
            return
        }

        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest, response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri: String = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map { obj: Cookie -> obj.value }
            .orElse(null)

        val targetUrl: String = redirectUri

        val mode: String = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
            .map { obj: Cookie -> obj.value }
            .orElse("")

        val principal: OAuth2UserPrincipal = getOAuth2UserPrincipal(authentication)
            ?: return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString()

        return when {
            "login".equals(mode, ignoreCase = true) -> {
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
                val accessToken = "test_access_token"
                val refreshToken = "test_refresh_token"
                UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString()
            }

            "unlink".equals(mode, ignoreCase = true) -> {
                val accessToken = principal.getUserInfo().accessToken
                val provider = principal.getUserInfo().provider
                oAuth2UserUnlinkManager.unlink(provider, accessToken)
                UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString()
            }

            else -> UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString()
        }
    }

    private fun getOAuth2UserPrincipal(authentication: Authentication): OAuth2UserPrincipal? {
        val principal = authentication.principal
        return if (principal is OAuth2UserPrincipal) {
            principal
        } else null
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    companion object {
        private const val MODE_PARAM_COOKIE_NAME = "mode"
        private const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
    }
}
