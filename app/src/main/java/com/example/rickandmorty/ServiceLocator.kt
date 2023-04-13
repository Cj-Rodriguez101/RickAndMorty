package com.example.rickandmorty

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.example.rickandmorty.network.CharNetwork
import com.example.rickandmorty.network.KtorClient
import com.example.rickandmorty.network.KtorLocationService
import com.example.rickandmorty.network.KtorLocationServiceImpl
import com.example.rickandmorty.network.RetrofitCharNetService
import com.example.rickandmorty.repository.CharRepository
import com.example.rickandmorty.repository.EpisodeRepository
import com.example.rickandmorty.repository.LocationRepository
import com.example.rickandmorty.util.Constants.BASE_URL

object ServiceLocator {

    //private var charNetwork: CharNetwork? = null
    private var retrofitCharNetService: RetrofitCharNetService? = null
    private var ktorLocationService: KtorLocationService? = null
    @Volatile
    var charRepository: CharRepository? = null
        @VisibleForTesting set

    var locationRepository: LocationRepository? = null
        @VisibleForTesting set

    var episodeRepository: EpisodeRepository? = null
        @VisibleForTesting set

    fun provideCharRepository(): CharRepository {
        synchronized(this) {
            val retrofitCharNetService = retrofitCharNetService ?: CharNetwork.charRestDb
            return CharRepository(retrofitCharNetService = retrofitCharNetService)
        }
    }

    fun provideLocationRepository(): LocationRepository {
        synchronized(this) {
            val ktorLocationServiceImpl = ktorLocationService ?:
            KtorLocationServiceImpl(KtorClient.ktorFinalClient,
                BASE_URL)
            return LocationRepository(ktorLocationService = ktorLocationServiceImpl)
        }
    }

    fun provideEpisodeRepository(): EpisodeRepository {
        synchronized(this) {
            val ktorLocationServiceImpl = ktorLocationService ?:
            KtorLocationServiceImpl(KtorClient.ktorFinalClient, BASE_URL
            )
            return EpisodeRepository(ktorLocationService = ktorLocationServiceImpl)
        }
    }
}