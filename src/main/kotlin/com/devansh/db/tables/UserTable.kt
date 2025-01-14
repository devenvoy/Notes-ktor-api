package com.devansh.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable : Table("note_user_table") {
    val id = varchar("id",255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }
    override val primaryKey = PrimaryKey(id)
}
