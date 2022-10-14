package com.martinz.myasteroid.data.remote

import com.martinz.myasteroid.data.model.PictureOfDay
import com.martinz.myasteroid.util.Constants
import com.squareup.moshi.Json
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.util.*

interface AsteroidApi {
    @GET("planetary/apod")
    suspend fun getPictureOfDay(
        @Query("api_key") apiKey : String = Constants.API_KEY
    ) : Response<PictureOfDay>


    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate : String ,
        @Query("end_date") endDate : String ,
        @Query("api_key") apiKey : String = Constants.API_KEY
    ) : Response<String>
}