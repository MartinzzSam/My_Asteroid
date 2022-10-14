package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import java.util.ArrayList

class GetTodayAsteroids(
    private val repository: AsteroidRepository,
    private val nextSevenDays : ArrayList<String>
) {
    suspend operator fun invoke() = repository.getSavedAsteroids().filter { it.closeApproachDate == nextSevenDays.first() }
}