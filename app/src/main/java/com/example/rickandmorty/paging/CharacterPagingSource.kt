package com.example.rickandmorty.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.network.RetrofitCharNetService

class CharacterPagingSource(
    private val backend: RetrofitCharNetService
    //val query: String
) : PagingSource<Int, Character>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Character> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = backend.getCharactersWithPage(nextPageNumber)

            LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = response.info.next?.let {
                    it.split("=")[1].toInt()
                }
            )
        } catch (ex: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
//            val charList: ArrayList<Character> = arrayListOf()
//            val nextPageNo = params.key?:1
//            ex.message?.contains("Unable to resolve host")?.let {
//                if(nextPageNo == 1){
//                    charList.add(Character(id = RETRY_HEADER))
//                }
//            }
//            if(charList.isEmpty()){
//                LoadResult.Error(ex)
//            } else {
//                LoadResult.Page(data = charList.toList(), null, null)
//            }
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
}