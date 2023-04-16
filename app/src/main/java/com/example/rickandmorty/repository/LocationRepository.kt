package com.example.rickandmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.model.MainLocation
import com.example.rickandmorty.network.KtorLocationService
import com.example.rickandmorty.paging.LocationPagingSource
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val ktorLocationService: KtorLocationService) {

    fun getPagedList(query: String): Flow<PagingData<MainLocation>> {

        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 20*3),
            pagingSourceFactory = { LocationPagingSource(ktorLocationService, query) }).flow

    }
}