package com.devansh.security

import io.ktor.server.auth.*

data class UserIdPrincipalForUser(val id:String): Principal
