package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.util.getNextSevenDaysFormattedDates

class GetWeekAsteroids(
    private val repository: AsteroidRepository
) {
   private val weekDay = getNextSevenDaysFormattedDates()
    suspend operator fun invoke() = repository.getSavedAsteroids().filter { it.closeApproachDate >= weekDay.first() }.sortedByDescending { it.closeApproachDate }
}