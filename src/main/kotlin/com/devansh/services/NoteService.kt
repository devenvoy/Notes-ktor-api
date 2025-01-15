package com.devansh.services

import com.devansh.db.DatabaseFactory.dbQuery
import com.devansh.db.tables.NoteTable
import com.devansh.model.NoteDto
import com.devansh.security.JwtConfig
import com.devansh.utils.BaseResponse
import com.devansh.utils.getUnAuthorizedResponse
import io.ktor.http.*
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

interface NoteService {
    suspend fun getAllNotes(authToken: String): BaseResponse<Any>
    suspend fun deleteNoteById(noteId: Long, authToken: String): BaseResponse<Any>
    suspend fun insertList(notes: List<NoteDto>, authToken: String): BaseResponse<Any>
    suspend fun updateById(noteDto: NoteDto, authToken: String): BaseResponse<Any>
}

class NoteServiceImpl : NoteService {

    override suspend fun getAllNotes(authToken: String): BaseResponse<Any> {
        val userId = JwtConfig.instance.getUserIdFromToken(authToken)
        if (userId.isNullOrBlank()) {
            return getUnAuthorizedResponse("Invalid Auth Token")
        }

        val notes = dbQuery {
            NoteTable.select { NoteTable.user_id eq userId }.orderBy(NoteTable.modifiedAt, SortOrder.DESC)
                .mapNotNull { rowToNoteDto(it) }
        }
        val count = notes.size

        return BaseResponse.SuccessResponse(
            isSuccessful = true,
            statusCode = HttpStatusCode.OK.value,
            data = mapOf(
                "total" to count,
                "notes" to notes,
            )
        )
    }

    override suspend fun deleteNoteById(noteId: Long, authToken: String): BaseResponse<Any> {
        val userId = JwtConfig.instance.getUserIdFromToken(authToken)
        if (userId.isNullOrBlank()) {
            return getUnAuthorizedResponse("Invalid Auth Token")
        }

        val rowsDeleted = dbQuery {
            NoteTable.deleteWhere { (note_id eq noteId) and (user_id eq userId) }
        }

        return if (rowsDeleted > 0) {
            BaseResponse.SuccessResponse(true, HttpStatusCode.OK.value, "Note deleted successfully")
        } else {
            BaseResponse.ErrorResponse(false, HttpStatusCode.NotFound.value, "Note not found or unauthorized")
        }
    }

    override suspend fun insertList(notes: List<NoteDto>, authToken: String): BaseResponse<Any> {
        val userId = JwtConfig.instance.getUserIdFromToken(authToken)
        if (userId.isNullOrBlank()) {
            return getUnAuthorizedResponse("Invalid Auth Token")
        }


        dbQuery {
            notes.forEach { note ->
                val parsedModifiedAt = if (!note.modifiedAt.isNullOrBlank()) {
                    try {
                        LocalDateTime.parse(note.modifiedAt)
                    } catch (e: DateTimeParseException) {
                        null
                    }
                } else {
                    null
                }
                NoteTable.upsert {
                    if (note.id.toInt() != -1) {
                        it[note_id] = note.id
                        parsedModifiedAt?.let { parsedDate ->
                            it[modifiedAt] = parsedDate
                        }
                    }
                    it[user_id] = userId
                    it[title] = note.title
                    it[content] = note.content
                    it[category] = note.category
                    it[colorres] = note.colorRes
                }
            }
        }

        return BaseResponse.SuccessResponse(true, HttpStatusCode.Created.value, "Notes inserted successfully")
    }

    override suspend fun updateById(noteDto: NoteDto, authToken: String): BaseResponse<Any> {
        val userId = JwtConfig.instance.getUserIdFromToken(authToken)
        if (userId.isNullOrBlank()) {
            return getUnAuthorizedResponse("Invalid Auth Token")
        }

        val rowsUpdated = dbQuery {
            NoteTable.update({ (NoteTable.note_id eq noteDto.id) and (NoteTable.user_id eq userId) }) {
                it[title] = noteDto.title
                it[content] = noteDto.content
                it[category] = noteDto.category
                it[colorres] = noteDto.colorRes
                it[modifiedAt] = LocalDateTime.now()
            }
        }

        return if (rowsUpdated > 0) {
            BaseResponse.SuccessResponse(true, HttpStatusCode.OK.value, "Note updated successfully")
        } else {
            BaseResponse.ErrorResponse(false, HttpStatusCode.NotFound.value, "Note not found or unauthorized")
        }
    }


    private fun rowToNoteDto(row: ResultRow): NoteDto {
        return NoteDto(
            id = row[NoteTable.note_id],
            title = row[NoteTable.title],
            content = row[NoteTable.content],
            category = row[NoteTable.category],
            colorRes = row[NoteTable.colorres],
            modifiedAt = row[NoteTable.modifiedAt].toString()
        )
    }
}


