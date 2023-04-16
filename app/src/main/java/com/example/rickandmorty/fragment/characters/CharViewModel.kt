package com.example.rickandmorty.fragment.characters

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class CharViewModel(private val charRepository: CharRepository) : ViewModel() {

    private val _selectedCharacter: MutableStateFlow<Character?> =
        MutableStateFlow<Character?>(null)
    val selectedCharacter: StateFlow<Character?> = _selectedCharacter

    private val _query: MutableStateFlow<String> = MutableStateFlow<String>("")

    val myMutablePagingFlow: Flow<PagingData<Character>> = _query.flatMapLatest {
        charRepository.getPagedList(it).cachedIn(viewModelScope).distinctUntilChanged()
    }


    fun setCharacter(character: Character?) {
        _selectedCharacter.value = character
    }

    fun setQuery(query: String) {
        _query.value = query
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    var myMutablePagingFlow = charRepository.getPagedList(query.value)
//        .flatMapLatest { searchCharacter(query.value) }
//        .cachedIn(viewModelScope)

//    fun searchCharacter(query: String): Flow<PagingData<Character>> {
//        return charRepository.getPagedList(query)
//    }
}

@Suppress("UNCHECKED_CAST")
class CharViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CharViewModel(ServiceLocator.provideCharRepository()) as T)
}