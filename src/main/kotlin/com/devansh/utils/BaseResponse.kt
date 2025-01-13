package com.devansh.utils

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
sealed interface BaseResponse<T> {
     val statusCode: Int
         get() = HttpStatusCode.OK.value


    @Serializable
    data class SuccessResponse<T>(
        val isSuccessful: Boolean = true,
        override val statusCode: Int = HttpStatusCode.OK.value,
        val message: String? = null,
        val data: T? = null
    ) : BaseResponse<T>

    @Serializable
    data class ErrorResponse<T>(
        val isSuccessful: Boolean = false,
        override val statusCode: Int = HttpStatusCode.OK.value,
        val message: String? = null,
        val data: T? = null
    ) : BaseResponse<T>
}