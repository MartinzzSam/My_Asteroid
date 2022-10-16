package com.martinz.myasteroid.data.repository

import androidx.room.withTransaction
import com.martinz.myasteroid.data.local.AsteroidDatabase
import com.martinz.myasteroid.data.model.Asteroid
import com.martinz.myasteroid.data.model.PictureOfDay
import com.martinz.myasteroid.data.remote.AsteroidApi
import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.util.Response
import com.martinz.myasteroid.util.networkBoundResource
import com.martinz.myasteroid.util.parseAsteroidsJsonResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class AsteroidRepositoryImpl @Inject constructor(
    private val api: AsteroidApi,
    private val database: AsteroidDatabase
) : AsteroidRepository {

    private val asteroidDao = database.asteroidDao()

    override suspend fun getPictureOfDay(): Flow<Response<PictureOfDay>> = flow {
        val response = api.getPictureOfDay()

        if (response.isSuccessful) {
            if (response.body() != null) {
                emit(Response.Success(response.body()!!))
            }
        } else {
            emit(Response.Error("Error Occurred"))
        }
    }

    override suspend fun getAsteroids(
        startDate: String,
        endDate: String
    ): Flow<Response<List<Asteroid>>> {

        return networkBoundResource(
            query = { asteroidDao.getAllAsteroid() },
            fetch = {
                parseAsteroidsJsonResult(
                    JSONObject(
                        api.getAsteroids(
                            startDate = startDate,
                            endDate = endDate
                        ).body()!!
                    )
                )
            },
            saveFetchResult = { asteroids ->
                database.withTransaction {
                    asteroidDao.deleteAllAsteroids()
                    asteroidDao.insertAsteroids(asteroids)
                }
            },
        )
    }

    override suspend fun getScheduledAsteroids(
        startDate: String,
        endDate: String
    ) {
        try {
            val fetchedList =  parseAsteroidsJsonResult(
                JSONObject(
                    api.getAsteroids(
                        startDate = startDate,
                        endDate = endDate
                    ).body()!!
                )
            )
            asteroidDao.deleteAllAsteroids()
            asteroidDao.insertAsteroids(fetchedList)
        } catch (e: Throwable) {
            throw e
        }
    }

    override suspend fun getSavedAsteroids(): List<Asteroid> = asteroidDao.getAllAsteroid().first()


}