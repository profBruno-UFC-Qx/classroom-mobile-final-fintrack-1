package com.example.fintrack.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun triggerTestNotification(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>().build()
        workManager.enqueue(workRequest)
    }

    fun scheduleReminders(context: Context) {
        val workManager = WorkManager.getInstance(context)

        // Agendar para 13h
        scheduleDailyWork(workManager, "reminder_13h", 13)
        
        // Agendar para 20h
        scheduleDailyWork(workManager, "reminder_20h", 20)
    }

    fun cancelReminders(context: Context) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelUniqueWork("reminder_13h")
        workManager.cancelUniqueWork("reminder_20h")
    }

    private fun scheduleDailyWork(workManager: WorkManager, tag: String, hour: Int) {
        val initialDelay = calculateDelay(hour)
        
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(tag)
            .build()

        workManager.enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun calculateDelay(hour: Int): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return calendar.timeInMillis - now
    }
}
