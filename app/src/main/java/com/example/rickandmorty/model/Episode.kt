package com.example.rickandmorty.model

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val id: Long,
    val name: String,
    //@Json(name = "air_date")
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
)
