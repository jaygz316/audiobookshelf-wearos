package com.jaybit.audiobookshelf

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val context = LocalContext.current
    val controllerState = remember { mutableStateOf<MediaController?>(null) }
    
    DisposableEffect(context) {
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        
        controllerFuture.addListener({
            controllerState.value = controllerFuture.get()
        }, MoreExecutors.directExecutor())
        
        onDispose {
            MediaController.releaseFuture(controllerFuture)
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Audiobookshelf WearOS")
            
            TitleCard(
                onClick = { /* TODO: Implement Login flow with AppAuth */ },
                title = { Text("Login") }
            ) {
                Text("Login via OIDC")
            }

            Button(
                onClick = { 
                    // Example playback trigger
                    // controllerState.value?.setMediaItem(mediaItem)
                    // controllerState.value?.prepare()
                    // controllerState.value?.play()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if (controllerState.value?.isPlaying == true) "Pause" else "Play Stream")
            }

            Button(onClick = { /* TODO: Trigger SyncWorker manually */ }) {
                Text("Manual Sync")
            }

            Button(onClick = { /* TODO: Navigate to SearchScreen */ }) {
                Text("Search")
            }
        }
    }
}

@Composable
fun SearchScreen() {
    // Placeholder for search implementation
    Text("Search Screen")
}
