package com.example.rickandmorty.fragment.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.model.MainLocation

class LocationListAdapter(private val onClick: (MainLocation) -> Unit) :
    PagingDataAdapter<MainLocation, LocationListAdapter.LocationViewHolder>(LocationDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_list_item, parent, false)
        return LocationViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class LocationViewHolder(itemView: View, onClick: (MainLocation) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.location_name)
        private val typeTextView: TextView = itemView.findViewById(R.id.type_name)
        private val dimensionTextView: TextView = itemView.findViewById(R.id.dimension_name)

        private var currentLocation: MainLocation? = null

        init {
            itemView.setOnClickListener {
                currentLocation?.let {
                    onClick(it)
                }
            }
        }

        fun bind(location: MainLocation) {
            currentLocation = location
            nameTextView.text = location.name
            typeTextView.text = location.type
            dimensionTextView.text = location.dimension
        }
    }
}

object LocationDiffCallback : DiffUtil.ItemCallback<MainLocation>() {
    override fun areItemsTheSame(oldItem: MainLocation, newItem: MainLocation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MainLocation, newItem: MainLocation): Boolean {
        return oldItem == newItem
    }
}