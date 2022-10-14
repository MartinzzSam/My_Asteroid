package com.martinz.myasteroid.persentation.main_fragment

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.google.gson.Gson
import com.martinz.myasteroid.data.remote.AsteroidApi
import com.martinz.myasteroid.domain.use_case.AsteroidUseCase
import com.martinz.myasteroid.persentation.AsteroidEvent
import com.martinz.myasteroid.persentation.AsteroidStatus
import com.martinz.myasteroid.util.Response
import com.martinz.myasteroid.util.parseAsteroidsJsonResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: AsteroidUseCase,
    private val api : AsteroidApi
) : ViewModel() {

    private val _liveData = MutableLiveData<AsteroidStatus>()
    val liveData : LiveData<AsteroidStatus> = _liveData

    val isLoading = MutableStateFlow(false)

    init {
        getPictureOfDay()
        getAsteroids()

    }







    fun onEvent(event : AsteroidEvent) {
        when(event) {

            is AsteroidEvent.GetWeekAsteroid -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _liveData.postValue(
                        liveData.value?.copy(
                            asteroids = useCase.GetWeekAsteroids()
                        )
                    )
                }

            }

            is AsteroidEvent.GetTodayAsteroid -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _liveData.postValue(
                        liveData.value?.copy(
                            asteroids = useCase.GetTodayAsteroids()
                        )
                    )
                }
            }


            is AsteroidEvent.GetSavedAsteroid -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _liveData.postValue(
                        liveData.value?.copy(
                            asteroids = useCase.GetSavedAsteroids()
                        )
                    )
                }
            }

        }
    }


    private fun getPictureOfDay() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                useCase.GetPictureOfDay().collect { response ->
                    when(response) {
                        is Response.Success -> {
                            _liveData.postValue(liveData.value?.copy(
                                pictureOfDay = response.data
                            ) ?: AsteroidStatus(
                                pictureOfDay = response.data
                            )
                            )
                        }

                        is Response.Error -> {
                            TODO()
                        }

                    }
                }
            } catch (e : Throwable) {
                Log.e("Log" , e.toString())
            }


        }
    }

    private fun getAsteroids() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.GetAsteroids().collect { response ->
                when(response) {

                    is Response.Loading -> {
                        isLoading.value = true
                    }
                    is Response.Success -> {
                        isLoading.value = false
                        Log.i("myLOg" , response.data.toString())
                        _liveData.postValue(liveData.value?.copy(
                            asteroids = response.data
                        ) ?: AsteroidStatus(
                            asteroids = response.data
                        ))
                    }

                    is Response.Error -> {
                        delay(2000L)
                        isLoading.value = false
                        _liveData.postValue(liveData.value?.copy(
                            asteroids = response.data
                        ) ?: AsteroidStatus(
                            asteroids = response.data
                        ))
                    }
                }
            }
        }
    }


//    private fun testApi() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
//            val date = LocalDate.parse("2022-10-14" , firstApiFormat)
//            val endDate = LocalDate.parse("2022-10-19" , firstApiFormat)
//
//            try {
//                val result = api.getAsteroids(startDate = date.toString() , endDate = endDate.toString())
//                Log.i("myLog" , result.body().toString())
//                if (result.isSuccessful) {
//                    val gson = JSONObject(result.body()!!)
//                    val list = parseAsteroidsJsonResult(gson)
//                    Log.i("myLogGson" , gson.toString() )
//                    Log.i("myLog" , list.toString() )
//                }
//            } catch (e : Throwable) {
//                Log.i("myLog" , e.toString())
//            }
//
//
//        }
//    }
}