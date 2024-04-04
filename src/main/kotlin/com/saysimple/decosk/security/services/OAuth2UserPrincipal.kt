package com.saysimple.decosk.security.services


import com.saysimple.decosk.security.users.OAuth2UserInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserPrincipal(private val userInfo: OAuth2UserInfo) : OAuth2User, UserDetails {

    override fun getPassword(): String? = null

    override fun getUsername(): String? = userInfo.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getAttributes(): Map<String, Any> = userInfo.attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getName(): String? = userInfo.email

    fun getUserInfo(): OAuth2UserInfo = userInfo
}
