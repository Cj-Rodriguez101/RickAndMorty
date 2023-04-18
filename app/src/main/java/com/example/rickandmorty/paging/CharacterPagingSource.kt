package com.example.rickandmorty.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.network.RetrofitChatNetServiceImpl
import com.example.rickandmorty.util.Constants.HTTP_404
import com.example.rickandmorty.util.Constants.PAGE

class CharacterPagingSource(
    private val backend: RetrofitChatNetServiceImpl,
    private val query: String
) : PagingSource<Int, Character>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Character> {
        return try {
            val pageNumber = params.key ?: 1
            val response = backend.searchCharactersWithPage(query, pageNumber)

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

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true
}