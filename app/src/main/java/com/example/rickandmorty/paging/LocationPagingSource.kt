package com.example.rickandmorty.paging

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.MainLocation
import com.example.rickandmorty.network.KtorLocationService

class LocationPagingSource(
    private val backend: KtorLocationService,
    private val query: String
) : PagingSource<Int, MainLocation>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MainLocation> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val response = backend.getFilteredLocations(query, pageNumber)

            //Log.e("location", response.results.map { it.id }.joinToString(","))

            LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = if(query.isEmpty()) Uri.parse(response.info.next).getQueryParameter("page")?.toInt() else null
            )
        } catch (ex: Exception) {
            if(ex.toString().contains("HTTP 404")){
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

    override val keyReuseSupported: Boolean
        get() = true
}