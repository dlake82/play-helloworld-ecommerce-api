package com.saysimple.decosk.security.oauth2.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*

object CookieUtils {

    fun getCookie(request: HttpServletRequest, name: String): Cookie? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == name }
    }

    fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
        val cookie = Cookie(name, value)
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = maxAge
        response.addCookie(cookie)
    }

    fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse?, name: String) {
        request.cookies?.filter { cookie -> cookie.name == name }?.forEach { cookie ->
            cookie.apply {
                value = ""
                path = "/"
                maxAge = 0
            }
            response?.addCookie(cookie)
        }
    }


    fun serialize(`object`: Any): String {
        return Base64.getUrlEncoder()
            .encodeToString(SerializationUtils.serialize(`object`))
    }

    fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
        return cls.cast(
            SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)
            )
        )
    }
}
