package com.saysimple.decosk.security.config

import com.example.oauth2.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.example.oauth2.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.example.oauth2.oauth2.service.CustomOAuth2UserService
import com.saysimple.decosk.security.HttpCookieOAuth2AuthorizationRequestRepository
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.AuthorizationEndpointConfig
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.UserInfoEndpointConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
class SecurityConfig {
    private val customOAuth2UserService: CustomOAuth2UserService? = null
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler? = null
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler? = null
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository? = null

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .headers { headersConfigurer: HeadersConfigurer<HttpSecurity?> ->
                headersConfigurer.frameOptions(
                    Customizer<FrameOptionsConfig> { FrameOptionsConfig.disable() })
            } // For H2 DB
            .formLogin { obj: FormLoginConfigurer<HttpSecurity> -> obj.disable() }
            .httpBasic { obj: HttpBasicConfigurer<HttpSecurity> -> obj.disable() }
            .authorizeHttpRequests(
                Customizer<AuthorizationManagerRequestMatcherRegistry> { requests: AuthorizationManagerRequestMatcherRegistry ->
                    requests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/**")).hasRole("ADMIN")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/user/**")).hasRole("USER")
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated()
                }
            )
            .sessionManagement { sessions: SessionManagementConfigurer<HttpSecurity?> ->
                sessions.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .oauth2Login { configure: OAuth2LoginConfigurer<HttpSecurity?> ->
                configure.authorizationEndpoint(
                    Customizer { config: AuthorizationEndpointConfig ->
                        config.authorizationRequestRepository(
                            httpCookieOAuth2AuthorizationRequestRepository
                        )
                    })
                    .userInfoEndpoint(Customizer { config: UserInfoEndpointConfig ->
                        config.userService(
                            customOAuth2UserService
                        )
                    })
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            }

        return http.build()
    }
}
