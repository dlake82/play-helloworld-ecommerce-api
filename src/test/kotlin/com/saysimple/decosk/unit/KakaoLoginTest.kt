import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
class KakaoLoginTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var kakaoService: KakaoService

    @Test
    fun `test kakao login`() {
        mockMvc.perform(get("/login/kakao"))
            .andExpect(status().isOk)
    }
}