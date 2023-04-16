package com.example.rickandmorty.network

import com.example.rickandmorty.model.EpisodeResults
import com.example.rickandmorty.model.LocationResults
import com.example.rickandmorty.model.MainLocation

interface KtorLocationService {

    suspend fun getLocations(page: Int): LocationResults
    suspend fun getFilteredLocations(query: String, page: Int): LocationResults

    suspend fun getEpisodes(page: Int): EpisodeResults
}