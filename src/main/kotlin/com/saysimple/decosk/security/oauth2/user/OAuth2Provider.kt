package com.saysimple.decosk.security.oauth2.user

enum class OAuth2Provider(val registrationId: String) {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github"),
    NAVER("naver"),
    KAKAO("kakao")
}
