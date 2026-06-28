package com.example.fintrack.model

data class News(
    val title: String,
    val description: String?,
    val link: String,
    val source_id: String?
)

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val results: List<News>
)
