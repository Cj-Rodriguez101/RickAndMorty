package com.example.rickandmorty.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Episode(
    val id: Long,
    val name: String,
    val air_date: String,
    val episode: String,
    val characters: List<String>,
    val url: String,
    val created: String
):Parcelable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Episode

        if (id != other.id) return false
        if (name != other.name) return false
        if (air_date != other.air_date) return false
        if (episode != other.episode) return false
        if (characters != other.characters) return false
        if (url != other.url) return false
        if (created != other.created) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + air_date.hashCode()
        result = 31 * result + episode.hashCode()
        result = 31 * result + characters.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + created.hashCode()
        return result
    }
}
