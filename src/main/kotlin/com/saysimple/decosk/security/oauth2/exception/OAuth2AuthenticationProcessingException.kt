package com.saysimple.decosk.security.oauth2.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException(msg: String?) : AuthenticationException(msg)
