package com.example.rickandmorty.fragment.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Episode

class EpisodeListAdapter(private val onClick: (Episode) -> Unit):
    PagingDataAdapter<Episode, EpisodeListAdapter.EpisodeListViewHolder>(EpisodeDiffCallback) {

    class EpisodeListViewHolder(itemView: View, onClick: (Episode) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val episodeNoTextView: TextView? = itemView.findViewById(R.id.episode_no)
        private val episodeNameTextView: TextView? = itemView.findViewById(R.id.episode_name)
        private val episodeDateTextView: TextView? = itemView.findViewById(R.id.episode_date)

        private var currentEpisode: Episode? = null

        init {
            itemView.setOnClickListener {
                currentEpisode?.let {
                    onClick(it)
                }
            }
        }

        /* Bind character and image. */
        fun bind(episode: Episode) {
            currentEpisode = episode

            episodeNoTextView?.text = episode.episode
            episodeNameTextView?.text = episode.name
            episodeDateTextView?.text = episode.air_date

        }
    }

    override fun onBindViewHolder(holder: EpisodeListViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        //return super.getItemViewType(position)
//        var viewType = 0
//        getItem(position)?.let {
//            holder.bind(it)
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.episode_list_item, parent, false)

        return EpisodeListViewHolder(view, onClick)
    }
}

object EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
    override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
        return oldItem.id == newItem.id
    }
}