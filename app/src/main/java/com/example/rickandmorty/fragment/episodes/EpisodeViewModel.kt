package com.example.rickandmorty.fragment.episodes

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.paging.cachedIn
import com.example.rickandmorty.ServiceLocator
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class EpisodeViewModel(private val episodeRepository: EpisodeRepository,
private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val EPISODE_KEY = "EpisodeKey"

    val myMutablePagingFlow = episodeRepository.getPagedList().cachedIn(viewModelScope)

    private val _selectedEpisode: MutableStateFlow<Episode?> = MutableStateFlow<Episode?>(null)
    val selectedEpisode: StateFlow<Episode?> = _selectedEpisode
    init {
        savedStateHandle.get<Episode>(EPISODE_KEY)?.let {
            _selectedEpisode.value = it
        }
    }

    fun setEpisode(episode: Episode?){
        savedStateHandle[EPISODE_KEY] = episode
        _selectedEpisode.value = episode
    }
}

@Suppress("UNCHECKED_CAST")
class EpisodeViewModelFactory()
    : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>,extras: CreationExtras) =
        (EpisodeViewModel(ServiceLocator.provideEpisodeRepository(), extras.createSavedStateHandle()) as T)
}