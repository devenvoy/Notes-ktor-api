package com.devansh.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthUserResponse(
    val userId : String,
    val email: String,
    val createdAt: String?,
    val updatedAt : String?
)
