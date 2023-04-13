package com.example.rickandmorty

//import android.R
import android.content.pm.ActivityInfo
import com.example.rickandmorty.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.fragment.characters.BaseCharacterFragment
import com.example.rickandmorty.fragment.characters.CharacterListFragment
import com.example.rickandmorty.fragment.episodes.BaseEpisodeFragment
import com.example.rickandmorty.fragment.location.LocationListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private var demoCollectionAdapter: DemoCollectionAdapter? = null
    private var callback: ViewPager2.OnPageChangeCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        if (demoCollectionAdapter == null) {
            demoCollectionAdapter = DemoCollectionAdapter(
                supportFragmentManager,
                lifecycle
            )
        }

        binding.fragContainer.isUserInputEnabled = false

        binding.fragContainer.adapter = demoCollectionAdapter

        binding.bottomNavigation.setOnItemSelectedListener {item->
            when(item.itemId){
                R.id.character_menu-> {
                    binding.fragContainer.setCurrentItem(0, true)
                }

                R.id.location_menu-> binding.fragContainer.setCurrentItem(1, true)

                R.id.episode_menu-> binding.fragContainer.setCurrentItem(2, true)
            }
            true
        }

        if (callback == null){
            callback = object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    if (position == 0){
                        if (binding.bottomNavigation.selectedItemId != R.id.character_menu){
                            binding.bottomNavigation.selectedItemId = R.id.character_menu
                        }
                    } else if(position == 1) {
                        if (binding.bottomNavigation.selectedItemId != R.id.location_menu){
                            binding.bottomNavigation.selectedItemId = R.id.location_menu
                        }
                    } else {
                        if (binding.bottomNavigation.selectedItemId != R.id.episode_menu){
                            binding.bottomNavigation.selectedItemId = R.id.episode_menu
                        }
                    }
                    super.onPageSelected(position)
                }
            }
        }
        binding.fragContainer.registerOnPageChangeCallback(callback!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        demoCollectionAdapter = null
        callback = null
    }

    fun changeOrientation(orientation: Int){
        requestedOrientation = orientation
    }
}

class DemoCollectionAdapter(fragmentManager: FragmentManager,
                            lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {

        return when(position){

            0-> {
                BaseCharacterFragment()
            }

            1-> {
                LocationListFragment()
            }

            else -> BaseEpisodeFragment()
        }
    }
}