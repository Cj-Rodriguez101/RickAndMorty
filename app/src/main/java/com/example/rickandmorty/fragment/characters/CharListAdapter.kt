package com.example.rickandmorty.fragment.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.util.Constants.ALIVE
import com.example.rickandmorty.util.Constants.UNKNOWN

class CharListAdapter(private val onClick: (Character) -> Unit) :
    PagingDataAdapter<Character, CharListAdapter.CharViewHolder>(CharDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.char_list_item, parent, false)

        return CharViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CharViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class CharViewHolder(itemView: View, onClick: (Character) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val charTextView: TextView? = itemView.findViewById(R.id.char_name)
        private val charStatus: TextView? = itemView.findViewById(R.id.char_status)
        private val charImageView: ImageView? = itemView.findViewById(R.id.char_image_list)

        private var currentCharacter: Character? = null

        init {
            itemView.setOnClickListener {
                currentCharacter?.let {
                    onClick(it)
                }
            }
        }

        /* Bind character and image. */
        fun bind(character: Character) {
            currentCharacter = character

            charTextView?.text = character.name
            charStatus?.text = character.status
            when (character.status) {
                ALIVE -> {
                    charStatus?.background =
                        itemView.context?.applicationContext?.let {
                            ResourcesCompat.getDrawable(it.resources, R.color.teal_200, null)
                        }
                }
                UNKNOWN -> {
                    charStatus?.background =
                        itemView.context?.applicationContext?.let {
                            ResourcesCompat.getDrawable(it.resources, R.color.orange, null)
                        }
                }
                else -> {
                    charStatus?.background =
                        itemView.context?.applicationContext?.let {
                            ResourcesCompat.getDrawable(it.resources, R.color.red, null)
                        }
                }
            }
            charImageView?.let { imageView ->
                Glide.with(itemView).load(character.image)
                    .centerCrop().placeholder(R.drawable.baseline_person_4_24).into(imageView)
            }
        }
    }
}

object CharDiffCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem == newItem
    }
}
