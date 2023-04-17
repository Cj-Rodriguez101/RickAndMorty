package com.example.rickandmorty.network

import com.example.rickandmorty.model.EpisodeResults
import com.example.rickandmorty.model.LocationResults
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class KtorServiceImpl(private val httpClient: HttpClient,
                      private val baseUrl: String): KtorService{

    override suspend fun getFilteredLocations(query: String, page: Int): LocationResults {
//        return if(query.isNotEmpty()) {
//            httpClient.get{
//                url("$baseUrl/location?name=$query")
//            }
//        }else {
//            httpClient.get{
//                url("$baseUrl/location?page=$page")
//            }
//        }

        return httpClient.get{
            url("$baseUrl/location?page=$page&name=$query")
        }
    }

    override suspend fun getEpisodes(query: String, page: Int): EpisodeResults {
//        return if(query.isNotEmpty()) {
//            httpClient.get{
//                url("$baseUrl/episode?name=$query")
//            }
//        }else {
//            httpClient.get{
//                url("$baseUrl/episode?page=$page")
//            }
//        }
        return httpClient.get{
            url("$baseUrl/episode?page=$page&name=$query")
        }
    }
}