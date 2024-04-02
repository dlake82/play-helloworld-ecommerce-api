import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.io.PrintWriter
import java.nio.charset.StandardCharsets

@Configuration
@EnableMethodSecurity
class SecurityConfig(private val oAuth2UserService: OAuth2UserService) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
        http.authorizeHttpRequests { config -> config.anyRequest().permitAll() }
        http.oauth2Login { oauth2Configurer ->
            oauth2Configurer
                .loginPage("/login")
                .successHandler(successHandler())
                .userInfoEndpoint()
                .userService(oAuth2UserService)
        }

        return http.build()
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        return AuthenticationSuccessHandler { request, response, authentication ->
            val defaultOAuth2User = authentication.principal as DefaultOAuth2User

            val id = defaultOAuth2User.attributes["id"].toString()
            val body = """
                    {"id":"$id"}
                    """.trimIndent()

            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = StandardCharsets.UTF_8.name()

            val writer = PrintWriter(response.writer)
            writer.println(body)
            writer.flush()
        }
    }
}
