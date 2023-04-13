package com.example.rickandmorty.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharResults (

  var info    : Info              = Info(),
  var results : List<Character> = listOf()
)