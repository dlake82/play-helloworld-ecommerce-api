import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController {
    @GetMapping
    fun get(): User {
        val user = User(1, "test")
        return user
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
