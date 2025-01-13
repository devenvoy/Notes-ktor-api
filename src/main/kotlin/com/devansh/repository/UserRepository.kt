package com.devansh.repository

import com.devansh.services.UserService
import com.devansh.services.params.LoginUserParams
import com.devansh.security.JwtConfig
import com.devansh.security.hash
import com.devansh.utils.BaseResponse
import io.ktor.http.*

interface UserRepository {
    suspend fun registerUser(params: LoginUserParams): BaseResponse<Any>
    suspend fun loginUser(loginUserParams: LoginUserParams): BaseResponse<Any>
}

class UserRepositoryImpl(
    private val service: UserService
) : UserRepository {
    override suspend fun registerUser(params: LoginUserParams): BaseResponse<Any> {
        return if (isUserExists(params.email)) {
            println("User already exists")
            BaseResponse.ErrorResponse(
                message = "Email already registered",
                statusCode = HttpStatusCode.BadRequest.value
            )
        } else {
            val user = service.registerUser(params)
            if (user != null) {
                val token = JwtConfig.instance.createAccessToken(user.userId)
                val hashMap = mutableMapOf("authToken" to token, "user" to user)
                BaseResponse.SuccessResponse(
                    message = "User registered successfully",
                    data = hashMap,
                    statusCode = HttpStatusCode.OK.value
                )
            } else {
                BaseResponse.ErrorResponse(
                    message = "something went wrong",
                    statusCode = HttpStatusCode.BadRequest.value
                )
            }
        }
    }

    override suspend fun loginUser(loginUserParams: LoginUserParams): BaseResponse<Any> {
        val user = service.findUserByEmail(loginUserParams.email)
        return if (user != null) {
            val storedHashedPassword = service.getStoredPasswordByEmail(loginUserParams.email)

            if (storedHashedPassword != null && checkPassword(loginUserParams.password, storedHashedPassword)) {
                val token = JwtConfig.instance.createAccessToken(user.userId)
                val hashMap = mutableMapOf("authToken" to token, "user" to user)
                BaseResponse.SuccessResponse(
                    message = "Login successful",
                    data = hashMap,
                    statusCode = HttpStatusCode.OK.value
                )
            } else {
                BaseResponse.ErrorResponse(
                    message = "Incorrect password",
                    statusCode = HttpStatusCode.Unauthorized.value
                )
            }
        } else {
            BaseResponse.ErrorResponse(
                message = "User not found",
                statusCode = HttpStatusCode.NotFound.value
            )
        }
    }


    private suspend fun isUserExists(email: String): Boolean {
        return service.findUserByEmail(email) != null
    }

    private fun checkPassword(inputPassword: String, storedHashedPassword: String): Boolean {
        return hash(inputPassword) == storedHashedPassword
    }
}