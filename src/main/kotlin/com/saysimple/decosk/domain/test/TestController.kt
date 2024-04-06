package com.saysimple.decosk.domain.test

import com.saysimple.decosk.domain.test.models.Post
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping
    fun get(): ResponseEntity<Post> {
        val test = Post(1)
        return ResponseEntity<Post>(test, HttpStatus.OK)
    }

    @PostMapping
    fun create(): ResponseEntity<Post> {
        val test = Post(1)
        return ResponseEntity<Post>(test, HttpStatus.OK)
    }

    @PutMapping
    fun update(id: Int): ResponseEntity<Post> {
        val test = Post(id)
        return ResponseEntity<Post>(test, HttpStatus.OK)
    }

    @DeleteMapping
    fun delete(): ResponseEntity<Post> {
        val test = Post(1)
        return ResponseEntity<Post>(test, HttpStatus.OK)
    }
}
