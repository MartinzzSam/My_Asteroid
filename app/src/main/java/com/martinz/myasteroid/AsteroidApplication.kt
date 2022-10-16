package com.martinz.myasteroid


import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.martinz.myasteroid.persentation.work.AsteroidsWorkManager
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AsteroidApplication : Application() ,  Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory



    override fun onCreate() {
        super.onCreate()
        doPeriodicWork()

    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    private fun doPeriodicWork() {
        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidsWorkManager>(
            1 ,
            TimeUnit.DAYS
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build()
        ).build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            AsteroidsWorkManager.WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}