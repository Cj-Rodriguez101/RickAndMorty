@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.rickandmorty.fragment.location

import android.app.SearchManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentLocationListBinding
import com.example.rickandmorty.util.PostsLoadStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocationListFragment : Fragment(), MenuProvider {

    lateinit var locationViewModel: LocationViewModel
    var adapter: LocationListAdapter? = null
    private var fragBinding: FragmentLocationListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationViewModel = ViewModelProvider(this, LocationViewModelFactory())[LocationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: FragmentLocationListBinding = FragmentLocationListBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        binding.locationToolbar.inflateMenu(R.menu.location_menu)

        binding.locationToolbar.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.apply {
            adapter = LocationListAdapter (){
                locationViewModel.setQuery("")
            }

            binding.locationRecyclerView.adapter = adapter!!.withLoadStateHeaderAndFooter(
                header = PostsLoadStateAdapter(adapter!!),
                footer = PostsLoadStateAdapter(adapter!!)
            )
            binding.locationRecyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        }

        binding.locationSwiperefresh.setOnRefreshListener {
            adapter?.retry()
            binding.locationSwiperefresh.isRefreshing = false
        }

        binding.locationRetryButton.setOnClickListener {
            adapter?.refresh()
        }

        lifecycleScope.launch {
            locationViewModel.myMutableFlow.collectLatest{
                adapter?.submitData(it)
            }
        }

        lifecycleScope.launch {
            locationViewModel.isLoading.collect{isLoading->
                if(isLoading){
                    binding.localProgressBar.visibility = View.VISIBLE
                } else {
                    binding.localProgressBar.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.map { it.refresh }
                ?.distinctUntilChanged()
                ?.collect {
                    if (it is LoadState.NotLoading) {
                        if(adapter?.itemCount == 0){
                            binding.isEmptyTextView.visibility = View.VISIBLE
                            binding.locationRecyclerView.visibility = View.GONE
                        } else {
                            binding.isEmptyTextView.visibility = View.GONE
                            binding.locationRecyclerView.visibility = View.VISIBLE
                        }

                    }
                }
        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.localProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.locationRetryButton.isVisible = loadStates.refresh !is LoadState.Loading
                        && loadStates.refresh is LoadState.Error
                        && loadStates.source.refresh is LoadState.Error
                binding.locationErrorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        fragBinding = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        //menuInflater.inflate(R.menu.location_menu, menu)

        val applicationContext = requireContext().applicationContext

        val searchManager = ContextCompat.getSystemService(
            applicationContext,
            SearchManager::class.java
        ) as SearchManager
        (menu.findItem(R.id.location_search_menu).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    locationViewModel.setQuery(newText?.trim()?:"")
                    fragBinding?.locationRecyclerView?.layoutManager?.scrollToPosition(0)
                    return true
                }
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.location_search_menu -> {
                // clearCompletedTasks()
                true
            }

            else -> false
        }
    }
}