package com.devansh.utils

import io.ktor.http.*
import io.ktor.server.routing.*

fun getUnAuthorizedResponse(message: String?= null): BaseResponse.ErrorResponse<Any> {
    return BaseResponse.ErrorResponse(
        isSuccessful = false,
        statusCode = HttpStatusCode.Unauthorized.value,
        message = message,
        data = null
    )
}

fun getBadRequestResponse(message: String?): BaseResponse.ErrorResponse<Nothing> {
    return BaseResponse.ErrorResponse(
        isSuccessful = false,
        statusCode = HttpStatusCode.BadRequest.value,
        message = message,
        data = null
    )
}

fun RoutingRequest.getAuthToken(): String? {
    return headers["Authorization"]?.removePrefix("Bearer ")?.trim()
}