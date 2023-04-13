package com.example.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.KtorLocationService

class EpisodePagingSource(
    private val backend: KtorLocationService,
    //val query: String
) : PagingSource<Int, Episode>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Episode> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = backend.getEpisodes(nextPageNumber)

            LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = response.info.next?.let {
                    it.split("=")[1].toInt()
                }
            )
        } catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}