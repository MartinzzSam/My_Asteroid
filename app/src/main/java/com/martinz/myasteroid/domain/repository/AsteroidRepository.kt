package com.martinz.myasteroid.domain.repository

import com.martinz.myasteroid.data.model.Asteroid
import com.martinz.myasteroid.data.model.PictureOfDay
import com.martinz.myasteroid.util.Response
import kotlinx.coroutines.flow.Flow

interface AsteroidRepository {


    suspend fun getPictureOfDay() : Flow<Response<PictureOfDay>>


    suspend fun getAsteroids(startDate : String , endDate:String) : Flow<Response<List<Asteroid>>>

    suspend fun getScheduledAsteroids(startDate: String , endDate: String)

    suspend fun getSavedAsteroids() : List<Asteroid>
}