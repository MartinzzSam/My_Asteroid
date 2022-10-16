package com.martinz.myasteroid.di

import android.app.Application
import androidx.room.Room
import com.martinz.myasteroid.BuildConfig
import com.martinz.myasteroid.data.local.AsteroidDatabase
import com.martinz.myasteroid.data.remote.AsteroidApi
import com.martinz.myasteroid.data.repository.AsteroidRepositoryImpl
import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.domain.use_case.*
import com.martinz.myasteroid.util.Constants.BASE_URL
import com.martinz.myasteroid.util.getNextSevenDaysFormattedDates
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideDatabase(app : Application) : AsteroidDatabase = Room.databaseBuilder(app ,
        AsteroidDatabase::class.java,
        "my_asteroid_database"
    ).build()

    @Singleton
    @Provides
    fun provideDao(dataBase : AsteroidDatabase) = dataBase.asteroidDao()

    @Singleton
    @Provides
    fun providesApiKey() : String = BuildConfig.API_KEY

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): AsteroidApi = retrofit.create(AsteroidApi::class.java)

    @Singleton
    @Provides
    fun providesRepository(apiService: AsteroidApi , dataBase: AsteroidDatabase) : AsteroidRepository = AsteroidRepositoryImpl(api = apiService , database =  dataBase)

    @Singleton
    @Provides
    fun provideNextSevenDaysFormattedDates() : ArrayList<String> = getNextSevenDaysFormattedDates()

    @Singleton
    @Provides
    fun provideAsteroidUseCase( nextSevenDays : ArrayList<String>,repository: AsteroidRepository) : AsteroidUseCase = AsteroidUseCase(
        GetPictureOfDay = GetPictureOfDay(repository),
        GetAsteroids = GetAsteroids(repository , nextSevenDays),
        GetWeekAsteroids = GetWeekAsteroids(repository),
        GetTodayAsteroids = GetTodayAsteroids(repository , nextSevenDays),
        GetSavedAsteroids = GetSavedAsteroids(repository),
        GetScheduledAsteroids = GetScheduledAsteroids(repository , nextSevenDays)

    )



}