package com.example.rickandmorty.network

import com.example.rickandmorty.model.CharResults
import com.example.rickandmorty.model.LocationResults
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetrofitCharNetService {

    @GET("character")
    suspend fun getCharactersWithPage(
        @Query("page") page: Int
    ): CharResults

    @GET("character")
    suspend fun searchCharactersWithPage(
        @Query("name") name: String
    ): CharResults

//    @GET("character")
//    suspend fun searchAndNewCharactersWithPage(
//        @Query("page") page: Int,
//        @Query("name") name: String
//    ): CharResults
}

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(0, TimeUnit.SECONDS)
    .writeTimeout(0, TimeUnit.SECONDS)
    .readTimeout(0, TimeUnit.SECONDS)
    .build()

object CharNetwork {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        //.addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        //.addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .build()

    val charRestDb: RetrofitCharNetService = retrofit.create(RetrofitCharNetService::class.java)
}