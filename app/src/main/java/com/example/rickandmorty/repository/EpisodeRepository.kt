package com.example.rickandmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.KtorLocationService
import com.example.rickandmorty.paging.EpisodePagingSource
import kotlinx.coroutines.flow.Flow

class EpisodeRepository(private val ktorLocationService: KtorLocationService) {

    fun getPagedList(): Flow<PagingData<Episode>> {

        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 20*3),
            pagingSourceFactory = { EpisodePagingSource(ktorLocationService) }).flow

    }
}