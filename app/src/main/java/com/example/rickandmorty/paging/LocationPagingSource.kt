package com.example.rickandmorty.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.MainLocation
import com.example.rickandmorty.network.KtorService
import com.example.rickandmorty.util.Constants.HTTP_404
import com.example.rickandmorty.util.Constants.PAGE

class LocationPagingSource(
    private val backend: KtorService,
    private val query: String
) : PagingSource<Int, MainLocation>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MainLocation> {
        return try {
            val pageNumber = params.key ?: 1
            val response = backend.getFilteredLocations(query, pageNumber)

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
            Log.e("dea", ex.toString())
            if(ex.toString().contains(HTTP_404)){
                return LoadResult.Page(
                    data = listOf(), null, null
                )
            }
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MainLocation>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}