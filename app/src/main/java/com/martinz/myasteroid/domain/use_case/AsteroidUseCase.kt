package com.martinz.myasteroid.domain.use_case



data class AsteroidUseCase(
    val GetPictureOfDay : GetPictureOfDay,
    val GetAsteroids : GetAsteroids,
    val GetWeekAsteroids : GetWeekAsteroids,
    val GetTodayAsteroids : GetTodayAsteroids,
    val GetSavedAsteroids : GetSavedAsteroids
)
