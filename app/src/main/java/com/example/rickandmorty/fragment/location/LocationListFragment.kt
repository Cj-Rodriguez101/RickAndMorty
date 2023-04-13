package com.example.rickandmorty.fragment.location

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.databinding.FragmentLocationListBinding
import com.example.rickandmorty.fragment.characters.CharListAdapter
import com.example.rickandmorty.fragment.characters.CharViewModel
import com.example.rickandmorty.fragment.characters.CharViewModelFactory
import com.example.rickandmorty.util.PostsLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest

class LocationListFragment : Fragment() {

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
    ): View? {

        val binding: FragmentLocationListBinding = FragmentLocationListBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        binding.apply {
            adapter = LocationListAdapter (){
                Log.e("click", it.name)
            }

            binding.locationRecyclerView.adapter = adapter!!.withLoadStateHeaderAndFooter(
                header = PostsLoadStateAdapter(adapter!!),
                footer = PostsLoadStateAdapter(adapter!!)
            )
            //binding.locationRecyclerView.adapter = adapter
            binding.locationRecyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
        }

        binding.locationSwiperefresh.setOnRefreshListener {
            adapter?.retry()
            binding.locationSwiperefresh.isRefreshing = false
        }

        binding.locationRetryButton.setOnClickListener {
            adapter?.refresh()
        }

        lifecycleScope.launchWhenCreated {
            locationViewModel.myMutableFlow.collect{
                adapter?.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            locationViewModel.isLoading.collect{isLoading->
                if(isLoading){
                    binding.localProgressBar.visibility = View.VISIBLE
                } else {
                    binding.localProgressBar.visibility = View.GONE
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.localProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.locationRetryButton.isVisible = loadStates.refresh !is LoadState.Loading && loadStates.refresh is LoadState.Error
                binding.locationErrorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        fragBinding = null
    }
}