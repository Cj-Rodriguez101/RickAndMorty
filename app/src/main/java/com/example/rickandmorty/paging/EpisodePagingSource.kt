package com.example.rickandmorty.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.network.KtorService
import com.example.rickandmorty.util.Constants
import com.example.rickandmorty.util.Constants.HTTP_404
import com.example.rickandmorty.util.Constants.PAGE

class EpisodePagingSource(
    private val backend: KtorService,
    private val query: String
) : PagingSource<Int, Episode>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Episode> {
        return try {
            val pageNumber = params.key ?: 1
            val response = backend.getEpisodes(query, pageNumber)

            var nextPageNumber: Int? = null

            if (response.info.next != null) {
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter(PAGE)
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = if(query.isEmpty()) nextPageNumber else null
            )
        } catch (ex: Exception) {
            if(ex.toString().contains(HTTP_404)){
                return LoadResult.Page(
                    data = listOf(), null, null
                )
            }
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