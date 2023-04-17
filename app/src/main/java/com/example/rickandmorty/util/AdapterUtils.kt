package com.example.rickandmorty.util

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R

class NetworkStateItemViewHolder(
    private val parent: ViewGroup,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {

    private val retryButtonView: Button = itemView.findViewById(R.id.retry_button)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
    private val errorMessage: TextView = itemView.findViewById(R.id.error_msg)

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            errorMessage.text = parent.context.applicationContext.getString(R.string.no_internet)
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retryButtonView.also {
            it.setOnClickListener { retryCallback() }
        }
        retryButtonView.isVisible = loadState is LoadState.Error
        errorMessage.isVisible = loadState is LoadState.Error
    }
}