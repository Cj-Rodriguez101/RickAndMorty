package com.example.rickandmorty.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Episode(
    val id: Long,
    val name: String,
    //@Json(name = "air_date")
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
):Parcelable
