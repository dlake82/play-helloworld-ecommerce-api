package com.saysimple.decosk.security.users

import com.example.oauth2.oauth2.user.OAuth2UserInfo

class KakaoOAuth2UserInfo(val accessToken: String, attributes: Map<String?, Any?>) :
    OAuth2UserInfo {
    private val attributes: MutableMap<String, Any?>?
    val id: String
    val email: String?
    val name: String?
    val firstName: String?
    val lastName: String?
    val nickname: String?
    val profileImageUrl: String?

    init {
        // attributes 맵의 kakao_account 키의 값에 실제 attributes 맵이 할당되어 있음
        val kakaoAccount = attributes["kakao_account"] as Map<String, Any>?
        val kakaoProfile = kakaoAccount!!["profile"] as MutableMap<String, Any?>?
        this.attributes = kakaoProfile

        this.id = (attributes["id"] as Long?).toString()
        this.email = kakaoAccount["email"] as String?

        this.name = null
        this.firstName = null
        this.lastName = null
        this.nickname = attributes["nickname"] as String?

        this.profileImageUrl = attributes["profile_image_url"] as String?

        this.attributes!!["id"] = id
        this.attributes["email"] = this.email
    }

    val provider: OAuth2Provider
        get() = OAuth2Provider.KAKAO

    fun getAttributes(): Map<String, Any?>? {
        return attributes
    }
}
