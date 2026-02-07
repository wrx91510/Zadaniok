package com.example.zadaniok.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false
)
