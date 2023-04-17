package com.example.rickandmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.KtorService
import com.example.rickandmorty.paging.EpisodePagingSource
import kotlinx.coroutines.flow.Flow

class EpisodeRepository(private val ktorService: KtorService) {

    fun getPagedList(query: String): Flow<PagingData<Episode>> {

        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 20*3),
            pagingSourceFactory = { EpisodePagingSource(ktorService, query) }).flow

    }
}