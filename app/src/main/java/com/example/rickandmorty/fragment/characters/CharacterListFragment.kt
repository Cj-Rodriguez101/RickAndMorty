package com.example.rickandmorty.fragment.characters

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
import androidx.core.content.ContextCompat.getSystemService
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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.util.PostsLoadStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class CharacterListFragment : Fragment(), MenuProvider
{

    @OptIn(ExperimentalCoroutinesApi::class)
    lateinit var charViewModel: CharViewModel
    private var adapter: CharListAdapter? = null
    private var fragBinding: FragmentCharacterListBinding? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charViewModel = ViewModelProvider(
            this.requireParentFragment(),
            CharViewModelFactory()
        )[CharViewModel::class.java]
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCharacterListBinding = FragmentCharacterListBinding
            .inflate(inflater, container, false)

        binding.toolbar.inflateMenu(R.menu.character_menu)

        binding.toolbar.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        fragBinding = binding

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.charToolbarLayout)
            .setupWithNavController(binding.toolbar, navController, appBarConfiguration)

        binding.apply {
            adapter = CharListAdapter() {
                charViewModel.setCharacter(it)
                charViewModel.setQuery("")
                findNavController().navigate(R.id.action_characterListFragment_to_characterSingleFragment)
            }
            binding.charRecyclerView.adapter = adapter!!.apply {
                withLoadStateHeaderAndFooter(
                    header = PostsLoadStateAdapter(adapter!!),
                    footer = PostsLoadStateAdapter(adapter!!)
                )
            }

            binding.charRecyclerView.layoutManager =
                GridLayoutManager(requireContext().applicationContext, 2)
        }

        binding.charSwiperefresh.setOnRefreshListener {
            adapter?.retry()
            binding.charSwiperefresh.isRefreshing = false
        }

        binding.charRetryButton.setOnClickListener {
            adapter?.refresh()
        }
        lifecycleScope.launch {
            charViewModel.myMutablePagingFlow.collectLatest { data ->
                adapter?.submitData(data)
            }
        }

        //https://stackoverflow.com/a/64526450
        lifecycleScope.launch {
            adapter?.loadStateFlow?.map { it.refresh }
                ?.distinctUntilChanged()
                ?.collect {
                    if (it is LoadState.NotLoading) {
                        if (adapter?.itemCount == 0) {
                            binding.isEmptyTextView.visibility = View.VISIBLE
                            binding.charRecyclerView.visibility = View.GONE
                        } else {
                            binding.isEmptyTextView.visibility = View.GONE
                            binding.charRecyclerView.visibility = View.VISIBLE
                        }

                    }
                }
        }

        lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { loadStates ->
                binding.charProgressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.charRetryButton.isVisible = loadStates.refresh !is LoadState.Loading
                        && loadStates.refresh is LoadState.Error
                        && loadStates.source.refresh is LoadState.Error
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

        @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        val applicationContext = requireContext().applicationContext

        val searchManager =
            getSystemService(applicationContext, SearchManager::class.java) as SearchManager
        (menu.findItem(R.id.character_search_menu).actionView as SearchView).apply {

            val searchEditText = findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            searchEditText.setTextColor(resources.getColor(R.color.white))
            searchEditText.setHintTextColor(resources.getColor(R.color.white))
            setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
            setIconifiedByDefault(true)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    charViewModel.setQuery(newText?.trim() ?: "")
                    return true
                }
            })
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        //Not Needed Since I Use Search Button As SearchView
        return when (menuItem.itemId) {
            R.id.character_search_menu -> {
                true
            }

            else -> false
        }
    }
}