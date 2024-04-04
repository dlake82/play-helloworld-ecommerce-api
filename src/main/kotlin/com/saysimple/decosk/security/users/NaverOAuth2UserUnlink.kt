package com.saysimple.decosk.security.users

import com.example.oauth2.oauth2.user.OAuth2UserUnlink
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@RequiredArgsConstructor
@Component
class NaverOAuth2UserUnlink : OAuth2UserUnlink {
    private val restTemplate: RestTemplate? = null

    @Value("\${spring.security.oauth2.client.registration.naver.client-id}")
    private val clientId: String? = null

    @Value("\${spring.security.oauth2.client.registration.naver.client-secret}")
    private val clientSecret: String? = null

    fun unlink(accessToken: String) {
        val url = URL +
                "?service_provider=NAVER" +
                "&grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken

        val response = restTemplate!!.getForObject(
            url,
            UnlinkResponse::class.java
        )

        if (response != null && !"success".equals(response.getResult(), ignoreCase = true)) {
            throw RuntimeException("Failed to Naver Unlink")
        }
    }

    @Getter
    @RequiredArgsConstructor
    class UnlinkResponse {
        @JsonProperty("access_token")
        private val accessToken: String? = null
        private val result: String? = null
    }

    companion object {
        private const val URL = "https://nid.naver.com/oauth2.0/token"
    }
}
