package com.example.rickandmorty.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class EpisodeResults (
    val info: Info= Info(),
    val results: List<Episode>
)