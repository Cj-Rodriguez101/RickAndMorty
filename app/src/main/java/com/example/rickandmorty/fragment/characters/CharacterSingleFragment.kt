package com.example.rickandmorty.fragment.characters

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentCharacterListBinding
import com.example.rickandmorty.databinding.FragmentCharacterSingleBinding

class CharacterSingleFragment : Fragment() {

    private val charViewModel: CharViewModel by viewModels({requireParentFragment()})
    private var fragBinding: FragmentCharacterSingleBinding? = null
    var backCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentCharacterSingleBinding = FragmentCharacterSingleBinding
            .inflate(inflater, container, false)
        fragBinding = binding

        val navController = findNavController()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.characterSingleFragment -> binding.singleToolbar.title = charViewModel.selectedCharacter.value!!.name
            }
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.singleToolbar as Toolbar)
            .setupWithNavController(navController, appBarConfiguration)

        lifecycleScope.launchWhenCreated {
            charViewModel.selectedCharacter.collect{character->
                character?.let {
                    Glide.with(binding.singleCharacterImage).load(character.image)
                        .centerCrop().placeholder(R.drawable.baseline_person_4_24).into(binding.singleCharacterImage)

                    binding.singleCharacterName.text = character.name
                    binding.singleCharacterStatus.text = character.status
                    if(character.status == "Alive"){
                        binding.singleCharacterStatus.setTextColor(Color.GREEN)
                    } else {
                        binding.singleCharacterStatus.setTextColor(Color.RED)
                    }
                    binding.singleCharacterGender.text = character.gender
                }
            }
        }

        if (backCallback == null){
            backCallback = object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    charViewModel.setCharacter(null)
                    findNavController().popBackStack() }
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