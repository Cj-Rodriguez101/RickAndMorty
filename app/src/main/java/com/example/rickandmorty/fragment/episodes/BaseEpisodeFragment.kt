package com.example.rickandmorty.fragment.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R

class BaseEpisodeFragment : Fragment() {

    lateinit var episodeViewModel: EpisodeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodeViewModel =
            ViewModelProvider(this, EpisodeViewModelFactory())[EpisodeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_episode, container, false)
    }
}