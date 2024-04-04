package com.saysimple.decosk.domain.users

import com.saysimple.decosk.domain.users.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController {
    @GetMapping
    fun get(): ResponseEntity<User> {
        val user = User(1, "test")
        return ResponseEntity<User>(user, HttpStatus.OK)
    }

    @PostMapping
    fun create(): String {
        return "admin"
    }

    @PutMapping
    fun update(): String {
        return "super admin"
    }

    @DeleteMapping
    fun delete(): String {
        return "deleted"
    }
}
