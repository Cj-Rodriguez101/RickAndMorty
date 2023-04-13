package com.example.rickandmorty.fragment.episodes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.databinding.FragmentEpisodeBinding
import com.example.rickandmorty.fragment.characters.CharListAdapter
import com.example.rickandmorty.util.PostsLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest

class EpisodeFragment : Fragment() {

    lateinit var episodeViewModel: EpisodeViewModel
    private var fragBinding: FragmentEpisodeBinding? = null
    var adapter: EpisodeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        episodeViewModel = ViewModelProvider(this.requireParentFragment(),
            EpisodeViewModelFactory())[EpisodeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_episode, container, false)

        val binding: FragmentEpisodeBinding = FragmentEpisodeBinding
            .inflate(inflater, container, false)
        fragBinding = binding
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.episodeToolbarLayout)
            .setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        binding.apply {
            adapter = EpisodeListAdapter (){
                findNavController().navigate(R.id.action_episodeFragment_to_episodeSingleFragment)
                episodeViewModel.setEpisode(it)
//                findNavController().navigate(R.id.action_characterListFragment_to_characterSingleFragment)
            }
            binding.episodeRecyclerView.adapter = adapter!!.withLoadStateHeaderAndFooter(
                header = PostsLoadStateAdapter(adapter!!),
                footer = PostsLoadStateAdapter(adapter!!)
            )

            binding.episodeRecyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        }

        binding.episodeSwiperefresh.setOnRefreshListener {
            //adapter?.refresh()
            adapter?.retry()
            binding.episodeSwiperefresh.isRefreshing = false
        }

        binding.episodeRetryButton.setOnClickListener {
            adapter?.refresh()
        }
        lifecycleScope.launchWhenCreated {
            episodeViewModel.myMutablePagingFlow.collect{data->
                adapter?.submitData(data)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.episodeProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.episodeRetryButton.isVisible = loadStates.refresh !is LoadState.Loading && loadStates.refresh is LoadState.Error && loadStates.source.refresh is LoadState.Error
                binding.episodeErrorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
        return binding.root
    }
}