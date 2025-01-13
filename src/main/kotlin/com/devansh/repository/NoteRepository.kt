package com.devansh.repository

import com.devansh.model.NoteDto
import com.devansh.services.NoteService
import com.devansh.utils.BaseResponse

interface NoteRepository {
    suspend fun getAllNotes(authToken: String): BaseResponse<Any>
    suspend fun deleteNoteById(noteId: Long, authToken: String): BaseResponse<Any>
    suspend fun insertList(notes: List<NoteDto>, authToken: String): BaseResponse<Any>
    suspend fun updateById(noteDto: NoteDto, authToken: String): BaseResponse<Any>
}


class NoteRepositoryImpl(private val noteService: NoteService) : NoteRepository {

    override suspend fun getAllNotes(authToken: String): BaseResponse<Any> {
        return noteService.getAllNotes(authToken)
    }

    override suspend fun deleteNoteById(noteId: Long, authToken: String): BaseResponse<Any> {
        return noteService.deleteNoteById(noteId, authToken)
    }

    override suspend fun insertList(notes: List<NoteDto>, authToken: String): BaseResponse<Any> {
        return noteService.insertList(notes, authToken)
    }

    override suspend fun updateById(noteDto: NoteDto, authToken: String): BaseResponse<Any> {
        return noteService.updateById( noteDto, authToken)
    }
}
