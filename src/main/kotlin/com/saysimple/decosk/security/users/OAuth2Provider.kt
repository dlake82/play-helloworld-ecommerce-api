package com.saysimple.decosk.security.users

import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
enum class OAuth2Provider(s: String) {
    GOOGLE("google"),

    //    FACEBOOK("facebook"),
//    GITHUB("github"),
    NAVER("naver"),
    KAKAO("kakao");

    val registrationId: String? = null
}
