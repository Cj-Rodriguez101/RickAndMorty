package com.example.rickandmorty.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.network.CharNetwork
import com.example.rickandmorty.network.RetrofitCharNetService
import com.example.rickandmorty.network.RetrofitChatNetServiceImpl
import com.example.rickandmorty.paging.CharacterPagingSource
import com.example.rickandmorty.util.toListCharacters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class CharRepository(private val retrofitCharNetService: RetrofitChatNetServiceImpl) {

     fun getPagedList(query: String): Flow<PagingData<Character>> {

        return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false, maxSize = 20*3),
            pagingSourceFactory = {CharacterPagingSource(retrofitCharNetService, query)}).flow

    }
}