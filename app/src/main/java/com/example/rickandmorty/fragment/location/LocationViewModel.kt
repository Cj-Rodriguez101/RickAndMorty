package com.example.rickandmorty.fragment.location

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.fragment.characters.CharViewModel
import com.example.rickandmorty.model.MainLocation
import com.example.rickandmorty.repository.LocationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepository: LocationRepository): ViewModel() {

    private val _query: MutableStateFlow<String> = MutableStateFlow<String>("")

    val myMutableFlow = _query.flatMapLatest {
        Log.e("mutable", it)
        locationRepository.getPagedList(it).cachedIn(viewModelScope).distinctUntilChanged()
    }

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setQuery(query: String) {
        _query.value = query
    }

}

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LocationViewModel(ServiceLocator.provideLocationRepository()) as T)
}