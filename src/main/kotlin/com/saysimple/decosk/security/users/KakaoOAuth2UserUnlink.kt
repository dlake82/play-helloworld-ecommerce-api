package com.saysimple.decosk.test.security.users


import lombok.RequiredArgsConstructor
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@RequiredArgsConstructor
@Component
class KakaoOAuth2UserUnlink : OAuth2UserUnlink {
    private val restTemplate: RestTemplate? = null

    override fun unlink(accessToken: String?) {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken!!)
        val entity = HttpEntity<Any>("", headers)

        restTemplate!!.exchange(
            URL, HttpMethod.POST, entity,
            String::class.java
        )
    }

    companion object {
        private const val URL = "https://kapi.kakao.com/v1/user/unlink"
    }
}
