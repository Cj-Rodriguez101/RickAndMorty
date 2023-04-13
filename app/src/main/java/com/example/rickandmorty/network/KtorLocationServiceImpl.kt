package com.example.rickandmorty.network

import com.example.rickandmorty.model.EpisodeResults
import com.example.rickandmorty.model.LocationResults
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class KtorLocationServiceImpl(private val httpClient: HttpClient,
                              private val baseUrl: String): KtorLocationService{
    override suspend fun getLocations(page: Int): LocationResults {
            return httpClient.get<LocationResults>{
                url("$baseUrl/location?page=$page")
            }
    }

    override suspend fun getEpisodes(page: Int): EpisodeResults {
        return httpClient.get<EpisodeResults>{
            url("$baseUrl/episode?page=$page")
        }
    }
}