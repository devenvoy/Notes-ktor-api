package com.devansh.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException

class JwtConfig private constructor(secret: String) {

    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withAudience(USER)
        .withIssuer(ISSUER)
        .build()

    fun createAccessToken(id:String):String = JWT.create()
        .withAudience(USER)
        .withIssuer(ISSUER)
        .withClaim(CLAIM,id)
        .sign(algorithm)


    fun getUserIdFromToken(token: String): String? =  try {
            val decodedJWT = verifier.verify(token)
            decodedJWT.getClaim(CLAIM).asString()
        } catch (e: JWTVerificationException) {
            null
        }


    companion object {
        private const val ISSUER = "notes-compose"
        private const val USER = "notes-compose-user"
        const val CLAIM = "id"

        lateinit var instance : JwtConfig
        private set

        fun initialize(secret: String) {
            synchronized(this){
                if(Companion::instance.isInitialized.not()){
                    instance = JwtConfig(secret)
                }
            }
        }
    }
}