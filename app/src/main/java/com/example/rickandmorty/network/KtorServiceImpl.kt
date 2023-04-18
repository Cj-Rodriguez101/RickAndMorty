package com.example.rickandmorty.network

import com.example.rickandmorty.model.EpisodeResults
import com.example.rickandmorty.model.LocationResults
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class KtorServiceImpl(private val httpClient: HttpClient,
                      private val baseUrl: String): KtorService{

    override suspend fun getFilteredLocations(query: String, page: Int): LocationResults {
        return httpClient.get{
            url("$baseUrl/location?page=$page&name=$query")
        }
    }

    override suspend fun getEpisodes(query: String, page: Int): EpisodeResults {
        return httpClient.get{
            url("$baseUrl/episode?page=$page&name=$query")
        }
    }
}