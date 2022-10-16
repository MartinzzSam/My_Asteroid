package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import java.util.ArrayList
import javax.inject.Inject

class GetScheduledAsteroids @Inject constructor(
    private val repository: AsteroidRepository,
    private val getNextSevenDay : ArrayList<String>
) {
    suspend operator fun invoke() = repository.getScheduledAsteroids(startDate = getNextSevenDay.first() , endDate = getNextSevenDay.last())
}