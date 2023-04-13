package com.example.rickandmorty.fragment.episodes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EpisodeViewModel(private val episodeRepository: EpisodeRepository): ViewModel() {

    val myMutablePagingFlow = episodeRepository.getPagedList().cachedIn(viewModelScope)

    private val _selectedEpisode: MutableStateFlow<Episode?> = MutableStateFlow<Episode?>(null)
    val selectedEpisode: StateFlow<Episode?> = _selectedEpisode

    fun setEpisode(episode: Episode?){
        _selectedEpisode.value = episode
    }
}

@Suppress("UNCHECKED_CAST")
class EpisodeViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        (EpisodeViewModel(ServiceLocator.provideEpisodeRepository()) as T)
}