package com.martinz.myasteroid.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martinz.myasteroid.data.model.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM asteroids")
    fun getAllAsteroid() : Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids : List<Asteroid>)

    @Query("DELETE FROM asteroids")
    suspend fun deleteAllAsteroids()

}