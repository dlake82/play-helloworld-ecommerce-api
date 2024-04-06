package com.saysimple.decosk.security.oauth2.user

import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Component
class GoogleOAuth2UserUnlink(private val restTemplate: RestTemplate) : OAuth2UserUnlink {

    companion object {
        private const val URL = "https://oauth2.googleapis.com/revoke"
    }

    override fun unlink(accessToken: String) {
        val params = LinkedMultiValueMap<String, String>()
        params.add("token", accessToken)
        restTemplate.postForObject(URL, params, String::class.java)
    }

}
