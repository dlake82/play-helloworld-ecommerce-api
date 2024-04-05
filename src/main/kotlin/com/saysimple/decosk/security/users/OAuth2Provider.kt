package com.saysimple.decosk.test.security.users

enum class OAuth2Provider(val registrationId: String) {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github"),
    NAVER("naver"),
    KAKAO("kakao")
}
