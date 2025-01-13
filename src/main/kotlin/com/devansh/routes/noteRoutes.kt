package com.devansh.routes

import com.devansh.model.NoteDto
import com.devansh.repository.NoteRepository
import com.devansh.utils.getAuthToken
import com.devansh.utils.getBadRequestResponse
import com.devansh.utils.getUnAuthorizedResponse
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Resource("/notes")
object Note {
    @Resource("/get-all")
    data class GetAllNotes(val parent: Note = Note)

    @Resource("/insert")
    data class InsertNotes(val parent: Note = Note)

    @Resource("/delete/{id}")
    data class DeleteNoteById(val id: Long?, val parent: Note = Note)

    @Resource("/update")
    data class UpdateNoteById(val parent: Note = Note)
}


fun Application.noteRoutes() {

    val noteRepository by inject<NoteRepository>()
    routing {
        get<Note.GetAllNotes> { _ ->
            val authToken = call.request.getAuthToken()
            if (authToken == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    getUnAuthorizedResponse("Auth token not found")
                )
                return@get
            }
            val response = noteRepository.getAllNotes(authToken)
            call.respond(HttpStatusCode.fromValue(response.statusCode), response)
        }

        post<Note.InsertNotes, List<NoteDto>> { _, notes ->
            val authToken = call.request.getAuthToken()
            if (authToken == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    getUnAuthorizedResponse("Auth token not found")
                )
                return@post
            }
            val response = noteRepository.insertList(notes, authToken)
            call.respond(HttpStatusCode.fromValue(response.statusCode), response)
        }

        delete<Note.DeleteNoteById> { request ->
            val authToken = call.request.getAuthToken()
            if (authToken == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    getUnAuthorizedResponse("Auth token not found")
                )
                return@delete
            }
            request.id?.let {
                val response = noteRepository.deleteNoteById(it, authToken)
                call.respond(HttpStatusCode.fromValue(response.statusCode), response)
            } ?: run {
                return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    getBadRequestResponse("Invalid or missing note ID")
                )
            }
        }

        put<Note.UpdateNoteById, NoteDto> { _, noteDto ->
            val authToken = call.request.getAuthToken()
            if (authToken == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    getUnAuthorizedResponse("Auth token not found")
                )
                return@put
            }
            val response = noteRepository.updateById(noteDto, authToken)
            call.respond(HttpStatusCode.fromValue(response.statusCode), response)
        }
    }
}
