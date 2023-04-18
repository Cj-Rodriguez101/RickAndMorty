package com.example.rickandmorty.fragment.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.R
import com.example.rickandmorty.databinding.FragmentEpisodeSingleBinding
import com.example.rickandmorty.util.VideoUtils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class EpisodeSingleFragment : Fragment() {

    private val episodeViewModel: EpisodeViewModel by viewModels({requireParentFragment()})
    private var youTubePlayerListener: YouTubePlayerListener? = null
    private var youTubePlayerOut: YouTubePlayer? = null
    private var fragBinding: FragmentEpisodeSingleBinding? = null
    private var isFullscreen = false
    var backCallback: OnBackPressedCallback? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentEpisodeSingleBinding = FragmentEpisodeSingleBinding
            .inflate(inflater, container, false)

        val iFramePlayerOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1).autoplay(1)
            .build()
        fragBinding = binding
        lifecycle.addObserver(binding.youtubePlayerView)

        val navController = findNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.episodeSingleFragment -> {
                    episodeViewModel.selectedEpisode.value?.let { episode->
                        binding.singleToolbar.title = episode.name
                        binding.name.text = episode.name
                        binding.number.text = episode.episode
                        binding.dateCreated.text = episode.air_date
                    }
                }
            }
        }
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        (binding.singleToolbar as Toolbar)
            .setupWithNavController(navController, appBarConfiguration)
        (binding.singleToolbar as Toolbar)
            .setNavigationOnClickListener{
                (requireActivity() as MainActivity).changeOrientation(false)
                navController.navigateUp()
            }

        youTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayerOut = youTubePlayer
                val videoId = episodeViewModel.selectedEpisode.value?.episode?.let{
                    VideoUtils.getVideoIdBasedOnEpisode(it)
                }?:"qtdCIs6JdXg"
                youTubePlayer.cueVideo(videoId, 0f)
            }
        }

        binding.youtubePlayerView.initialize((youTubePlayerListener!!), iFramePlayerOptions)

        if (backCallback == null){
            backCallback = object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {

                    episodeViewModel.setQuery("")
                    (requireActivity() as MainActivity).changeOrientation(false)
                    if (isFullscreen) {
                        // if the player is in fullscreen, exit fullscreen
                        youTubePlayerOut?.toggleFullscreen()
                        // the video will continue playing in the player
                        binding.singleToolbar.visibility = View.VISIBLE
                    } else {
                        episodeViewModel.setEpisode(null)
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                (requireActivity() as MainActivity).changeOrientation(true)

                // the video will continue playing in fullscreenView
                binding.singleToolbar.visibility = View.GONE
                binding.youtubePlayerView.visibility = View.GONE
                binding.fullScreenViewContainer.visibility = View.VISIBLE
                binding.fullScreenViewContainer.addView(fullscreenView)
            }

            override fun onExitFullscreen() {
                isFullscreen = false
                (requireActivity() as MainActivity).changeOrientation(false)

                // the video will continue playing in the player
                binding.singleToolbar.visibility = View.VISIBLE
                binding.youtubePlayerView.visibility = View.VISIBLE
                binding.fullScreenViewContainer.visibility = View.GONE
                binding.fullScreenViewContainer.removeAllViews()
            }
        })


        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, backCallback!!)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        youTubePlayerOut = null
        fragBinding?.youtubePlayerView?.let {
            it.release()
            lifecycle.removeObserver(it)
        }
        youTubePlayerListener?.let {
            fragBinding?.youtubePlayerView?.removeYouTubePlayerListener(it)
        }
        backCallback = null
    }
}