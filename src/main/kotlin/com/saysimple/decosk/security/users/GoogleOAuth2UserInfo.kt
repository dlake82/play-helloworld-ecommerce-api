package com.saysimple.decosk.security.users

import com.example.oauth2.oauth2.user.OAuth2Provider
import com.example.oauth2.oauth2.user.OAuth2UserInfo

class GoogleOAuth2UserInfo(val accessToken: String, val attributes: Map<String, Any>) : OAuth2UserInfo {
    val id: String? = attributes["sub"] as String?
    val email: String? = attributes["email"] as String?
    val name: String? = attributes["name"] as String?
    val firstName: String? = attributes["given_name"] as String?
    val lastName: String? = attributes["family_name"] as String?
    val nickname: String? = null
    val profileImageUrl: String? = attributes["picture"] as String?

    val provider: OAuth2Provider
        get() = OAuth2Provider.GOOGLE
}
