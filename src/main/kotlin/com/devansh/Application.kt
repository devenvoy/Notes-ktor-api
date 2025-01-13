package com.devansh

import com.devansh.db.DatabaseFactory
import com.devansh.repository.UserRepository
import com.devansh.repository.UserRepositoryImpl
import com.devansh.routes.authRoutes
import com.devansh.security.configureSecurity
import com.devansh.services.UserService
import com.devansh.services.UserServiceImpl
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module).start(wait = true)
}

val modules = module {
    single<UserService> { UserServiceImpl() }
    single<UserRepository> { UserRepositoryImpl(get()) }
}

fun Application.module() {

    DatabaseFactory.init()

    install(Koin) {
        slf4jLogger()
        modules(modules)
    }

    install(Resources)
    install(ContentNegotiation) {
        jackson()
    }

//    configureHTTP()
    configureMonitoring()
    configureSecurity()
    authRoutes()
}

