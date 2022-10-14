package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.util.getNextSevenDaysFormattedDates
import javax.inject.Inject

class GetAsteroids @Inject constructor(
    private val repository: AsteroidRepository
) {
    private val dateList = getNextSevenDaysFormattedDates()

    suspend operator fun invoke() = repository.getAsteroids(startDate = dateList.first() , endDate = dateList.last())
}