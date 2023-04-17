package com.example.rickandmorty.network

import com.example.rickandmorty.model.EpisodeResults
import com.example.rickandmorty.model.LocationResults

interface KtorService {
    suspend fun getFilteredLocations(query: String, page: Int): LocationResults
    suspend fun getEpisodes(query: String, page: Int): EpisodeResults
}