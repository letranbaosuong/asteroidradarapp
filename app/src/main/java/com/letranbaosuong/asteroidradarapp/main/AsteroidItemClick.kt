package com.letranbaosuong.asteroidradarapp.main

import androidx.recyclerview.widget.DiffUtil
import com.letranbaosuong.asteroidradarapp.models.Asteroid

class AsteroidItemDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem == newItem
    }
}

class AsteroidItemClick(val asteroidItem: (Asteroid) -> Unit) {
    fun onTap(asteroid: Asteroid) = asteroidItem(asteroid)
}