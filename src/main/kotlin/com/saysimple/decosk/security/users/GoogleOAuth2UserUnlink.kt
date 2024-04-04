package com.saysimple.decosk.security.users

import com.example.oauth2.oauth2.user.OAuth2UserUnlink
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@RequiredArgsConstructor
@Component
class GoogleOAuth2UserUnlink : OAuth2UserUnlink {
    private val restTemplate: RestTemplate? = null

    fun unlink(accessToken: String?) {
        val params: MultiValueMap<String, String> = LinkedMultiValueMap()
        params.add("token", accessToken)
        restTemplate!!.postForObject(URL, params, String::class.java)
    }

    companion object {
        private const val URL = "https://oauth2.googleapis.com/revoke"
    }
}
