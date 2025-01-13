package com.devansh.services

import com.devansh.db.DatabaseFactory.dbQuery
import com.devansh.db.tables.UserTable
import com.devansh.model.AuthUserResponse
import com.devansh.security.hash
import com.devansh.services.params.LoginUserParams
import com.devansh.utils.generateUUID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

interface UserService {
    suspend fun findUserByEmail(email: String): AuthUserResponse?
    suspend fun registerUser(params: LoginUserParams): AuthUserResponse?
    suspend fun getStoredPasswordByEmail(email: String): String?
}

class UserServiceImpl : UserService {

    override suspend fun findUserByEmail(email: String): AuthUserResponse? {
        val user = dbQuery {
            UserTable.select { UserTable.email eq email }
                .map { rowToUser(it) }.singleOrNull()
        }
        println("User find: $user")
        return user
    }

    override suspend fun registerUser(params: LoginUserParams): AuthUserResponse? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = UserTable.insert {
                it[id] = generateUUID()
                it[email] = params.email
                it[password] = hash(params.password)
            }
        }
        return rowToUser(statement?.resultedValues?.get(0))
    }

    override suspend fun getStoredPasswordByEmail(email: String): String? {
        return dbQuery {
            UserTable
                .select { UserTable.email eq email }
                .map { it[UserTable.password] }
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow?): AuthUserResponse? {
        return if (row == null) null
        else {
            AuthUserResponse(
                userId = row[UserTable.id],
                email = row[UserTable.email],
                createdAt = row[UserTable.createdAt].toString(),
                updatedAt = row[UserTable.updatedAt].toString()
            )
        }
    }
}