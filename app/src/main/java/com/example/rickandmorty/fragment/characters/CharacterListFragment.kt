package com.example.rickandmorty.fragment.characters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.util.PostsLoadStateAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.coroutines.flow.collectLatest

class CharacterListFragment : Fragment() {

    lateinit var charViewModel: CharViewModel
    var adapter: CharListAdapter? = null
    private var fragBinding: FragmentCharacterListBinding? = null
    //private val charViewModel: CharViewModel by viewModels({requireParentFragment()})
    //private val charViewModel: CharViewModel = (requireParentFragment() as BaseCharacterFragment).charViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //charViewModel = ViewModelProvider(this, CharViewModelFactory())[CharViewModel::class.java]
        charViewModel = ViewModelProvider(this.requireParentFragment(), CharViewModelFactory())[CharViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCharacterListBinding = FragmentCharacterListBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.charToolbarLayout)
            .setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        binding.apply {
            adapter = CharListAdapter(){
                //Log.e("click", it.name)
                charViewModel.setCharacter(it)
                findNavController().navigate(R.id.action_characterListFragment_to_characterSingleFragment)
                //val navController = NavHostFragment.findNavController(this)
            }
            binding.charRecyclerView.adapter = adapter!!.apply {
                withLoadStateHeaderAndFooter(
                header = PostsLoadStateAdapter(adapter!!),
                footer = PostsLoadStateAdapter(adapter!!))
            }

            binding.charRecyclerView.layoutManager = GridLayoutManager(requireContext().applicationContext, 3)
        }

        binding.charSwiperefresh.setOnRefreshListener {
            //adapter?.refresh()
            adapter?.retry()
            binding.charSwiperefresh.isRefreshing = false
        }

        binding.charRetryButton.setOnClickListener {
            adapter?.refresh()
        }
        lifecycleScope.launchWhenCreated {
            charViewModel.myMutablePagingFlow.collect{data->
                adapter?.submitData(data)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.charProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.charRetryButton.isVisible = loadStates.refresh !is LoadState.Loading && loadStates.refresh is LoadState.Error && loadStates.source.refresh is LoadState.Error
                binding.charErrorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        fragBinding = null
    }
}