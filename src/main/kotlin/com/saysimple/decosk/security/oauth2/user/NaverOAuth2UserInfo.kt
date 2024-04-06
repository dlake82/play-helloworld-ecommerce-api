package com.saysimple.decosk.security.oauth2.user


class NaverOAuth2UserInfo(override val accessToken: String, attributes: Map<String?, Any>) :
    OAuth2UserInfo {
    // attributes 맵의 response 키의 값에 실제 attributes 맵이 할당되어 있음
    override val attributes: Map<String?, Any> = attributes["response"] as Map<String?, Any>
    override val id: String? = this.attributes["id"] as String?
    override val email: String? = this.attributes["email"] as String?
    override val name: String? = this.attributes["name"] as String?
    override val firstName: String? = null
    override val lastName: String? = null
    override val nickname: String? = attributes["nickname"] as String?

    override val profileImageUrl: String? = attributes["profile_image"] as String?

    override val provider: OAuth2Provider
        get() = OAuth2Provider.NAVER
}
