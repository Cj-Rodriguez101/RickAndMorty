package com.example.rickandmorty.fragment.characters

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterSingleBinding
import com.example.rickandmorty.util.Constants.ALIVE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CharacterSingleFragment : Fragment() {

    private val charViewModel: CharViewModel by viewModels({ requireParentFragment() })
    private var fragBinding: FragmentCharacterSingleBinding? = null
    var backCallback: OnBackPressedCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentCharacterSingleBinding = FragmentCharacterSingleBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        val navController = findNavController()
        navController.addOnDestinationChangedListener { _,destination,_ ->
            when (destination.id) {
                R.id.characterSingleFragment -> binding.singleToolbar.title =
                    charViewModel.selectedCharacter.value!!.name
            }
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.singleToolbar as Toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        lifecycleScope.launch {
            charViewModel.selectedCharacter.collectLatest { character ->
                character?.let {
                    Glide.with(binding.singleCharacterImage).load(character.image)
                        .centerCrop().placeholder(R.drawable.baseline_person_4_24)
                        .into(binding.singleCharacterImage)

                    binding.singleCharacterName.text = character.name
                    binding.singleCharacterStatus.text = character.status
                    if (character.status == ALIVE) {
                        binding.singleCharacterStatus.setTextColor(Color.GREEN)
                    } else {
                        binding.singleCharacterStatus.setTextColor(Color.RED)
                    }
                    binding.singleCharacterGender.text = character.gender
                }
            }
        }

        if (backCallback == null) {
            backCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    charViewModel.setQuery("")
                    charViewModel.setCharacter(null)
                    findNavController().popBackStack()
                }
            }
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback!!)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backCallback = null
    }
}