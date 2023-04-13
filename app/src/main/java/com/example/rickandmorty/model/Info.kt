package com.example.rickandmorty.model

import com.squareup.moshi.JsonClass
import kotlinx.serialization.Serializable

@JsonClass(generateAdapter = true)
@Serializable
data class Info (

  var count : Int?    = null,
  var pages : Int?    = null,
  var next  : String? = null,
  var prev  : String? = null

)