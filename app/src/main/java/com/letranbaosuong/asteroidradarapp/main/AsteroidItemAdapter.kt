package com.letranbaosuong.asteroidradarapp.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.letranbaosuong.asteroidradarapp.databinding.AsteroidItemBinding
import com.letranbaosuong.asteroidradarapp.models.Asteroid

class AsteroidItemAdapter(
    private val callback: AsteroidItemClick
) : ListAdapter<Asteroid, AsteroidItemAdapter.AsteroidItemViewHolder>(AsteroidItemDiffCallback()) {

    class AsteroidItemViewHolder(val binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AsteroidItemBinding.inflate(inflater, parent, false)
        return AsteroidItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidItemViewHolder, position: Int) {
        holder.binding.asteroid = getItem(position)
        holder.binding.asteroidItemCallback = callback
    }
}
