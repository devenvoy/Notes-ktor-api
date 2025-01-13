package com.devansh.routes

import com.devansh.repository.UserRepository
import com.devansh.services.params.LoginUserParams
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Resource("/auth")
object Auth {

    @Resource("/register")
    data class Register(val parent : Auth = Auth)

    @Resource("/login")
    data class Login(val parent : Auth = Auth)

}

fun Application.authRoutes() {
    val userRepository by inject<UserRepository>()
    routing {
        post<Auth.Register,LoginUserParams> { _, body ->
            try {
                val result = userRepository.registerUser(body)
                call.respond(HttpStatusCode.fromValue(result.statusCode), result)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }
        post<Auth.Login,LoginUserParams> { _ , body ->
            try {
                val result = userRepository.loginUser(body)
                call.respond(HttpStatusCode.fromValue(result.statusCode), result)
            }catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.localizedMessage))
            }
        }
    }
}