package com.example.zadaniok.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val title = inputData.getString("title") ?: "Zadaniok"
        val desc = inputData.getString("desc")
            ?: "Masz zadanie do wykonania"

        NotificationUtils.show(
            applicationContext,
            "Przypomnienie: $title",
            desc
        )

        return Result.success()
    }
}
