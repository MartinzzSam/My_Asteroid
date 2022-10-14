package com.martinz.myasteroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.martinz.myasteroid.data.model.Asteroid

@Database(entities = [Asteroid::class], version = 1 , )

abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDao() : AsteroidDao
}