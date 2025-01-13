package com.devansh.services.params

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserParams(
    val email: String,
    val password: String,
)
