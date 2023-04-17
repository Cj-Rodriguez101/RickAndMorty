package com.example.rickandmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.network.RetrofitChatNetServiceImpl
import com.example.rickandmorty.paging.CharacterPagingSource
import kotlinx.coroutines.flow.Flow

class CharRepository(private val retrofitCharNetService: RetrofitChatNetServiceImpl) {

    fun getPagedList(query: String): Flow<PagingData<Character>> {

        return Pager(config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
            maxSize = 20 * 3
        ),
            pagingSourceFactory = { CharacterPagingSource(retrofitCharNetService, query) }).flow

    }
}