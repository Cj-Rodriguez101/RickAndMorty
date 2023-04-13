package com.example.rickandmorty.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class Character (
  val id       : Int = 0,
  val name     : String = "",
  val status   : String = "",
  val species  : String = "",
  val type     : String = "",
  val gender   : String = "",
  val origin   : Origin = Origin("", ""),
  val location : Location = Location(),
  val image    : String = "",
  val episode  : List<String> = listOf(),
  val url      : String = "",
  val created  : String = ""
): Parcelable