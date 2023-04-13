package com.example.rickandmorty.fragment.characters

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CharViewModel (private val charRepository: CharRepository): ViewModel() {

    val myMutablePagingFlow = charRepository.getPagedList().cachedIn(viewModelScope)

    private val _selectedCharacter: MutableStateFlow<Character?> = MutableStateFlow<Character?>(null)
    val selectedCharacter: StateFlow<Character?> = _selectedCharacter

    fun setCharacter(character: Character?){
        _selectedCharacter.value = character
    }
}

@Suppress("UNCHECKED_CAST")
class CharViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (CharViewModel(ServiceLocator.provideCharRepository()) as T)
}