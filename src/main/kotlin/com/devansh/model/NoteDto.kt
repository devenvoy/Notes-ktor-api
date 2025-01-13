package com.devansh.model

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: Long,
    val title: String,
    val content: String,
    val category: String? = null,
    val modifiedAt: String? = null,
)
