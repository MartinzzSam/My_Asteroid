package com.martinz.myasteroid.persentation.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.martinz.myasteroid.data.local.AsteroidDatabase
import com.martinz.myasteroid.data.repository.AsteroidRepositoryImpl
import com.martinz.myasteroid.domain.repository.AsteroidRepository
import com.martinz.myasteroid.domain.use_case.AsteroidUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class AsteroidsWorkManager @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workManagerParams: WorkerParameters,
    private val useCase: AsteroidUseCase
) : CoroutineWorker(context, workManagerParams) {
    companion object {
        const val WORKER_NAME = "AsteroidWorker"
    }
    override suspend fun doWork(): Result {

        Log.i("Hilt", "Doing Work")
        try {
            useCase.GetScheduledAsteroids()
            return Result.success()

        } catch (e: Throwable) {
            return Result.failure()
        }
    }
}