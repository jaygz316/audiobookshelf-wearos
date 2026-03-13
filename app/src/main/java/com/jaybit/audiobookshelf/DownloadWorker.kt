package com.jaybit.audiobookshelf

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * DownloadWorker handles background downloading of audiobook files.
 */
class DownloadWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val downloadUrl = inputData.getString("url") ?: return@withContext Result.failure()
        val itemId = inputData.getString("id") ?: return@withContext Result.failure()

        val targetDir = File(applicationContext.filesDir, "audiobooks")
        if (!targetDir.exists()) targetDir.mkdirs()
        val targetFile = File(targetDir, "$itemId.mp3")

        try {
            val url = URL(downloadUrl)
            val conn = (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 15_000
                readTimeout = 15_000
                requestMethod = "GET"
            }

            conn.connect()
            if (conn.responseCode !in 200..299) {
                conn.disconnect()
                return@withContext Result.retry()
            }

            BufferedInputStream(conn.inputStream).use { input ->
                FileOutputStream(targetFile).use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var bytes = input.read(buffer)
                    while (bytes >= 0) {
                        output.write(buffer, 0, bytes)
                        bytes = input.read(buffer)
                    }
                    output.flush()
                }
            }
            conn.disconnect()

            // Note: app database update (Room) should be done by caller or via a Repository.
            return@withContext Result.success()
        } catch (e: java.io.IOException) {
            // Network or IO issue - retry
            return@withContext Result.retry()
        } catch (e: Exception) {
            // Unknown issue - fail to avoid infinite retry loops
            return@withContext Result.failure()
        }
    }
}
