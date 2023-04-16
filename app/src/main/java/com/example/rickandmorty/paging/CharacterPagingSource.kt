package com.example.rickandmorty.paging

import android.net.Uri
import android.util.Log
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.network.RetrofitCharNetService
import com.example.rickandmorty.network.RetrofitChatNetServiceImpl
import retrofit2.HttpException

class CharacterPagingSource(
    private val backend: RetrofitChatNetServiceImpl,
    private val query: String
) : PagingSource<Int, Character>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Character> {
        return try {
            // Start refresh at page 1 if undefined.
            val pageNumber = params.key ?: 1
            val response = backend.searchCharactersWithPage(query, pageNumber)

            //Log.e("good", "no ${response.results.map { it.id }.joinToString(",")}")

            //var nextPageNumber: Int? = null

//            if (response.info.next != null) {
//                val uri = Uri.parse(response.info.next)
//                val nextPageQuery = uri.getQueryParameter("page")
//                nextPageNumber = nextPageQuery?.toInt()
//            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
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

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true
}