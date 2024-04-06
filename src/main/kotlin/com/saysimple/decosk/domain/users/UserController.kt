package com.saysimple.decosk.domain.users

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UserController {

    @GetMapping("/users")
    fun getUserInfo(@AuthenticationPrincipal userDetails: UserDetails?): ResponseEntity<String> {
        return userDetails?.let {
            ResponseEntity.ok(it.username)
        } ?: ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
