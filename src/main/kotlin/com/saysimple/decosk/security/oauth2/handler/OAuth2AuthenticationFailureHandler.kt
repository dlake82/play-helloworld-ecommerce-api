package com.saysimple.decosk.security.oauth2.handler

import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
import com.saysimple.decosk.security.oauth2.utils.CookieUtils
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException

@RequiredArgsConstructor
@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {


    @Throws(IOException::class)
    override fun onAuthenticationFailure(
        request: HttpServletRequest, response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var targetUrl: String = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
            .map { obj: Cookie -> obj.value }
            .orElse(("/"))

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("error", exception.localizedMessage)
            .build().toUriString()

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}
