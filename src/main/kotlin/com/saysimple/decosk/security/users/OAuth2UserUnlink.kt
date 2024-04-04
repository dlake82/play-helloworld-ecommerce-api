package com.saysimple.decosk.security.users

interface OAuth2UserUnlink {
    fun unlink(accessToken: String?)
}
