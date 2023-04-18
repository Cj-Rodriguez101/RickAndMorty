package com.example.rickandmorty.network

import com.example.rickandmorty.model.CharResults

class RetrofitChatNetServiceImpl(private val retrofitCharNetService: RetrofitCharNetService) {

    suspend fun searchCharactersWithPage(name: String, page: Int): CharResults {
        return retrofitCharNetService.searchAndNewCharactersWithPage(page, name)
    }
}