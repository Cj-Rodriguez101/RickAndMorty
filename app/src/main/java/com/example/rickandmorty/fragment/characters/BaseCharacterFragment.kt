package com.example.rickandmorty.fragment.characters

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmorty.R

class BaseCharacterFragment : Fragment() {

    lateinit var charViewModel: CharViewModel
    //private val charViewModel: CharViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        charViewModel = ViewModelProvider(this, CharViewModelFactory())[CharViewModel::class.java]
        //charViewModel = ViewModelProvider(this, CharViewModelFactory())[CharViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base_character, container, false)
    }
}