package com.example.rickandmorty.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class LocationResults (
    val info : Info = Info(),
    val results: List<MainLocation>
)

@Serializable
data class MainLocation (
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>,
    val url: String,
    val created: String
)