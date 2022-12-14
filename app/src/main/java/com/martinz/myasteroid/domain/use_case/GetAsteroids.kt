package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.util.getNextSevenDaysFormattedDates
import java.util.ArrayList
import javax.inject.Inject

class GetAsteroids @Inject constructor(
    private val repository: AsteroidRepository,
    private val getNextSevenDay : ArrayList<String>
) {

    suspend operator fun invoke() = repository.getAsteroids(startDate = getNextSevenDay.first() , endDate = getNextSevenDay.last())
}