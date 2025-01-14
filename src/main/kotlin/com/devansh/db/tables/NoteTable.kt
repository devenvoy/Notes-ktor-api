package com.devansh.db.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object NoteTable : Table(name = "note_table") {
    val note_id = long("note_id").autoIncrement()
    val user_id = varchar("user_id", 255).references(UserTable.id)
    val title = varchar("title", 255)
    val content = text("content")
    val category = varchar("category", 255).nullable()
    val colorres = long(name = "color_res").nullable()
    val modifiedAt = datetime("modified_at").clientDefault { LocalDateTime.now() }
    override val primaryKey = PrimaryKey(note_id)
}