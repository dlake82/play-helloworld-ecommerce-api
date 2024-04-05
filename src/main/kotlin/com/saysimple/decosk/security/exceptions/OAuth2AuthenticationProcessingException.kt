package com.example.oauth2.oauth2.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(msg: String?) : AuthenticationException(msg)
