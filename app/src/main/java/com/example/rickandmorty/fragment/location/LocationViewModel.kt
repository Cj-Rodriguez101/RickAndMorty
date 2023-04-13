package com.example.rickandmorty.fragment.location

import android.content.Context
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
import kotlinx.coroutines.launch

class LocationViewModel(private val locationRepository: LocationRepository): ViewModel() {

    //private val _myMutableFlow = MutableStateFlow<List<MainLocation>>(listOf())
    val myMutableFlow = locationRepository.getPagedList().cachedIn(viewModelScope)

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

//    fun updateList(page:Int) {
//        viewModelScope.launch {
//            CoroutineScope(Dispatchers.IO).launch {
//                _isLoading.value = true
//                _myMutableFlow.value = locationRepository.getPagedList(page)
//                _isLoading.value = false
//            }
//        }
//    }

//    init {
//        updateList(1)
//    }
}

@Suppress("UNCHECKED_CAST")
class LocationViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (LocationViewModel(ServiceLocator.provideLocationRepository()) as T)
}