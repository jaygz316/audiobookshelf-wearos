package com.jaybit.audiobookshelf

import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import android.content.Intent
import android.os.IBinder
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory

/**
 * PlaybackService manages the ExoPlayer and MediaSession for the Audiobookshelf WearOS app.
 */
class PlaybackService : MediaSessionService() {
    private var player: ExoPlayer? = null
    private var mediaSession: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        
        // 1. Initialize ExoPlayer with HLS support
        val dataSourceFactory = DefaultHttpDataSource.Factory()
            .setAllowCrossProtocolRedirects(true)
            .setUserAgent("AudiobookshelfWearOS/1.0")

        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH) // Optimized for audiobooks
                    .setUsage(C.USAGE_MEDIA)
                    .build(),
                true
            )
            .build()

        // 2. Initialize MediaSession
        player?.let {
            mediaSession = MediaSession.Builder(this, it).build()
        }
    }

    /**
     * Helper to load and start playback of a stream.
     */
    fun startStreaming(url: String, authToken: String?) {
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType("application/x-mpegURL") // Force HLS
            .build()

        player?.let {
            it.setMediaItem(mediaItem)
            it.prepare()
            it.play()
        }
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player?.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}
