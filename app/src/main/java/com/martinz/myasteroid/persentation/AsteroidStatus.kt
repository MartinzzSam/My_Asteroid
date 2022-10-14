package com.martinz.myasteroid.persentation

import com.martinz.myasteroid.data.model.Asteroid
import com.martinz.myasteroid.data.model.PictureOfDay
import com.martinz.myasteroid.domain.use_case.GetPictureOfDay

data class AsteroidStatus(
    val pictureOfDay: PictureOfDay? = null,
    val asteroids : List<Asteroid>? =null
)
