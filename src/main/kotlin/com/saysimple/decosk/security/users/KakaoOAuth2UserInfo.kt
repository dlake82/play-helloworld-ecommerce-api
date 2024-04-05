package com.saysimple.decosk.test.security.users


class KakaoOAuth2UserInfo(
    override val accessToken: String,
    attributes: Map<String, Any>
) : OAuth2UserInfo {
    override val attributes: Map<String, Any>
    override val id: String
    override val email: String?
    override val name: String?
    override val firstName: String?
    override val lastName: String?
    override val nickname: String?
    override val profileImageUrl: String?

    init {
        val kakaoAccount = attributes["kakao_account"] as MutableMap<String, Any>
        val kakaoProfile = kakaoAccount["profile"] as MutableMap<String, Any>
        this.attributes = kakaoProfile

        this.id = (attributes["id"] as Long?).toString()
        this.email = kakaoAccount["email"] as String?

        this.name = null
        this.firstName = null
        this.lastName = null
        this.nickname = attributes["nickname"] as String?
        this.profileImageUrl = attributes["profile_image_url"] as String?

        this.attributes["id"] = id
        this.attributes["email"] = this.email ?: ""
    }

    override val provider: OAuth2Provider
        get() = OAuth2Provider.KAKAO
}
