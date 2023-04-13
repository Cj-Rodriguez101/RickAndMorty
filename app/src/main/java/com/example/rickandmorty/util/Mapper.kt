package com.example.rickandmorty.util

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharResults

fun CharResults.toListCharacters(): List<Character>{
    return this.results
}