import com.nimbusds.jose.shaded.gson.JsonObject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("login")
class AuthController {
    @GetMapping
    fun login(): String {
        return "login"
    }
    @GetMapping("logout")
    fun logout(): String {
        val obj: JsonObject = JsonObject()

        obj.addProperty("title", "테스트3")
        obj.addProperty("content", "테스트3 내용")

        val data: JsonObject = JsonObject()

        data.addProperty("time", "12:00")

        obj.add("data", data)

        return obj.toString()
    }
}
