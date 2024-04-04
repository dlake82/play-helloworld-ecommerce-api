package com.saysimple.decosk.security.users


class GoogleOAuth2UserInfo(override val accessToken: String, override val attributes: Map<String, Any>) :
    OAuth2UserInfo {
    override val id: String? = attributes["sub"] as String?
    override val email: String? = attributes["email"] as String?
    override val name: String? = attributes["name"] as String?
    override val firstName: String? = attributes["given_name"] as String?
    override val lastName: String? = attributes["family_name"] as String?
    override val nickname: String? = null
    override val profileImageUrl: String? = attributes["picture"] as String?

    override val provider: OAuth2Provider
        get() = OAuth2Provider.GOOGLE
}
