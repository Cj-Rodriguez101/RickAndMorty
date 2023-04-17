package com.example.rickandmorty.fragment.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.repository.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest

@ExperimentalCoroutinesApi
class LocationViewModel(private val locationRepository: LocationRepository): ViewModel() {

    private val _query: MutableStateFlow<String> = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    val myMutableFlow = _query.flatMapLatest {
        locationRepository.getPagedList(it).cachedIn(viewModelScope).distinctUntilChanged()
    }

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun setQuery(query: String) {
        _query.value = query
    }

}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LocationViewModel(ServiceLocator.provideLocationRepository()) as T)
}