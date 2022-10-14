package com.martinz.myasteroid.persentation

import com.martinz.myasteroid.data.model.Asteroid

class OnClickListener(val clickListener: (asteroid : Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}