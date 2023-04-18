package com.example.rickandmorty.fragment.episodes

import android.app.SearchManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentEpisodeBinding
import com.example.rickandmorty.util.PostsLoadStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class EpisodeFragment : Fragment(), MenuProvider {

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
    ): View {

        val binding: FragmentEpisodeBinding = FragmentEpisodeBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        binding.toolbar.inflateMenu(R.menu.episode_menu)

        binding.toolbar.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.episodeToolbarLayout)
            .setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        binding.apply {
            adapter = EpisodeListAdapter (){
                findNavController().navigate(R.id.action_episodeFragment_to_episodeSingleFragment)
                episodeViewModel.setEpisode(it)
                episodeViewModel.setQuery("")
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
        lifecycleScope.launch {
            episodeViewModel.myMutablePagingFlow.collectLatest{data->
                adapter?.submitData(data)
            }
        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.map { it.refresh }
                ?.distinctUntilChanged()
                ?.collect {
                    if (it is LoadState.NotLoading) {
                        if(adapter?.itemCount == 0){
                            binding.isEmptyTextView.visibility = View.VISIBLE
                            binding.episodeRecyclerView.visibility = View.GONE
                        } else {
                            binding.isEmptyTextView.visibility = View.GONE
                            binding.episodeRecyclerView.visibility = View.VISIBLE
                        }

                    }
                }
        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.episodeProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.episodeRetryButton.isVisible = loadStates.refresh !is LoadState.Loading
                        && loadStates.refresh is LoadState.Error
                        && loadStates.source.refresh is LoadState.Error
                binding.episodeErrorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        //menuInflater.inflate(R.menu.episode_menu, menu)

        val applicationContext = requireContext().applicationContext

        val searchManager = ContextCompat.getSystemService(
            applicationContext,
            SearchManager::class.java
        ) as SearchManager
        (menu.findItem(R.id.episode_search_menu).actionView as SearchView).apply {

            val searchEditText = findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(resources.getColor(R.color.white))
            searchEditText.setHintTextColor(resources.getColor(R.color.white))
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    fragBinding?.episodeRecyclerView?.layoutManager?.scrollToPosition(0)
                    episodeViewModel.setQuery(newText?.trim()?:"")
                    return true
                }
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.episode_search_menu -> {
                // clearCompletedTasks()
                true
            }

            else -> false
        }
    }
}