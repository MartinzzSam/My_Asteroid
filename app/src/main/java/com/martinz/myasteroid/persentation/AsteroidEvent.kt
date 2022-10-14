package com.martinz.myasteroid.persentation

sealed class AsteroidEvent {
    object GetWeekAsteroid : AsteroidEvent()
    object GetTodayAsteroid : AsteroidEvent()
    object GetSavedAsteroid : AsteroidEvent()
}
