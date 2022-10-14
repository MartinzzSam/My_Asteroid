package com.martinz.myasteroid.domain.use_case

import com.martinz.myasteroid.domain.repository.AsteroidRepository
import javax.inject.Inject

class GetPictureOfDay @Inject constructor(
    private val repository: AsteroidRepository
) {

    suspend operator fun invoke() = repository.getPictureOfDay()
}