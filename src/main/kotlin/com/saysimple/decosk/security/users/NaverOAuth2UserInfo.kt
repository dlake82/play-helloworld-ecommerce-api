package com.saysimple.decosk.security.users

import com.example.oauth2.oauth2.user.OAuth2Provider
import com.example.oauth2.oauth2.user.OAuth2UserInfo

class NaverOAuth2UserInfo(val accessToken: String, attributes: Map<String?, Any?>) :
    OAuth2UserInfo {
    // attributes 맵의 response 키의 값에 실제 attributes 맵이 할당되어 있음
    val attributes: Map<String, Any>? = attributes["response"] as Map<String, Any>?
    val id: String? = this.attributes!!["id"] as String?
    val email: String? = this.attributes!!["email"] as String?
    val name: String? = this.attributes!!["name"] as String?
    val firstName: String? = null
    val lastName: String? = null
    val nickname: String? = attributes["nickname"] as String?

    val profileImageUrl: String? = attributes["profile_image"] as String?

    val provider: OAuth2Provider
        get() = OAuth2Provider.NAVER
}
