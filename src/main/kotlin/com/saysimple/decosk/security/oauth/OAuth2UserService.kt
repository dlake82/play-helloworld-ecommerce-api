import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class OAuth2UserService : DefaultOAuth2UserService() {
    @Throws(OAuth2AuthenticationException::class)
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        // Role generate
        val authorities: List<GrantedAuthority> = AuthorityUtils.createAuthorityList("ROLE_ADMIN")

        // nameAttributeKey
        val userNameAttributeName = userRequest.clientRegistration
                .providerDetails
                .userInfoEndpoint
                .userNameAttributeName

        // DB 저장로직이 필요하면 추가

        return DefaultOAuth2User(authorities, oAuth2User.attributes, userNameAttributeName)
    }
}