package com.martinz.myasteroid.persentation.main_fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martinz.myasteroid.data.remote.AsteroidApi
import com.martinz.myasteroid.domain.use_case.AsteroidUseCase
import com.martinz.myasteroid.persentation.AsteroidEvent
import com.martinz.myasteroid.persentation.AsteroidStatus
import com.martinz.myasteroid.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCase: AsteroidUseCase,
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

}