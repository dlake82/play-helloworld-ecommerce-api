package com.saysimple.decosk.domain.posts

import com.saysimple.decosk.domain.test.models.Post
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/post")
class PostController {

    @GetMapping
    fun index(): ResponseEntity<Post> {
        val post = Post(1)
        return ResponseEntity<Post>(post, HttpStatus.OK)
    }
}
