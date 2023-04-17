package com.example.rickandmorty.network

import android.util.Log
import com.example.rickandmorty.model.CharResults

class RetrofitChatNetServiceImpl(private val retrofitCharNetService: RetrofitCharNetService) {

    suspend fun searchCharactersWithPage(name: String, page: Int): CharResults {
//        return if(name.isEmpty()){
//            retrofitCharNetService.getCharactersWithPage(page)
//        } else {
//            retrofitCharNetService.searchCharactersWithPage(name)
//        }
        return retrofitCharNetService.searchAndNewCharactersWithPage(page, name)
    }
}