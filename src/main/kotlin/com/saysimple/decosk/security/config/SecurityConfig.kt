package com.saysimple.decosk.security.config

import com.saysimple.decosk.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.saysimple.decosk.security.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.saysimple.decosk.security.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.saysimple.decosk.security.oauth2.service.CustomOAuth2UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

val log = KotlinLogging.logger {}

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        log.info { "Start filterChain" }

        http.csrf { csrfConfigurer -> csrfConfigurer.disable() }
            .headers { headersConfigurer -> headersConfigurer.frameOptions { frameOptionsConfig -> frameOptionsConfig.disable() } } // For H2 DB
            .formLogin { formLoginConfigurer -> formLoginConfigurer.disable() }
            .httpBasic { httpBasicConfigurer -> httpBasicConfigurer.disable() }
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers(AntPathRequestMatcher("/api/admin/**")).hasRole("ADMIN")
                    .requestMatchers(AntPathRequestMatcher("/api/user/**")).hasRole("USER")
                    .requestMatchers(AntPathRequestMatcher("/h2-console/**")).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .oauth2Login { configure ->
                configure.authorizationEndpoint { config ->
                    config.authorizationRequestRepository(
                        httpCookieOAuth2AuthorizationRequestRepository
                    )
                }
                    .userInfoEndpoint { config -> config.userService(customOAuth2UserService) }
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
            }

        log.info { "End filterChain" }
        return http.build()
    }
}
