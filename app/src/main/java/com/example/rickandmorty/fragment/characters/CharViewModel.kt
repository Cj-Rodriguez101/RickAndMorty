package com.example.rickandmorty.fragment.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest


@ExperimentalCoroutinesApi
class CharViewModel(private val charRepository: CharRepository) : ViewModel() {

    private val _selectedCharacter: MutableStateFlow<Character?> =
        MutableStateFlow<Character?>(null)
    val selectedCharacter: StateFlow<Character?> = _selectedCharacter

    private val _query: MutableStateFlow<String> = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    val myMutablePagingFlow: Flow<PagingData<Character>> = _query.flatMapLatest {
        charRepository.getPagedList(it).cachedIn(viewModelScope).distinctUntilChanged()
    }


    fun setCharacter(character: Character?) {
        _selectedCharacter.value = character
    }

    fun setQuery(query: String) {
        _query.value = query
    }
}

@Suppress("UNCHECKED_CAST")
class CharViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CharViewModel(ServiceLocator.provideCharRepository()) as T)
}