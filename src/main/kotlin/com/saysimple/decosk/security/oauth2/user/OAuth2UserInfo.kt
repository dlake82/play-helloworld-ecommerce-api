package com.saysimple.decosk.security.oauth2.user

interface OAuth2UserInfo {
    val provider: OAuth2Provider

    val accessToken: String

    val attributes: Map<String, Any>

    val id: String

    val email: String

    val name: String?

    val firstName: String?

    val lastName: String?

    val nickname: String?

    val profileImageUrl: String?
}
