package com.devansh.db

import com.devansh.db.tables.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(UserTable,NoteTable)
        }
    }
    private fun hikari() : HikariDataSource {
        val config = HikariConfig()
        config.apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("JDBC_DATABASE_URL") ?: throw IllegalArgumentException("JDBC_DATABASE_URL is not set")
            username = System.getenv("JDBC_USERNAME") ?: throw IllegalArgumentException("JDBC_DATABASE_URL is not set")
            password = System.getenv("JDBC_PASSWORD") ?: throw IllegalArgumentException("JDBC_DATABASE_URL is not set")
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block:()->T):T= withContext(Dispatchers.IO){
        transaction {
            block()
        }
    }
}