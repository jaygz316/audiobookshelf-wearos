# Audiobookshelf WearOS - Streaming Implementation

## Overview
This document outlines the strategy for implementing audio streaming in the WearOS app using Android Media3 (ExoPlayer).

## API Endpoints (Audiobookshelf)
- **Start Playback:** `POST /api/items/{id}/play`
  - Returns: Playback session details, including HLS manifest URL or direct stream URL.
- **Sync Progress:** `POST /api/me/progress/{id}`
  - payload: `{"currentTime": 123.45, "isFinished": false}`
- **Get Stream:** `GET /api/items/{id}/stream`
  - Query Params: `token={token}`

## Media3 Architecture
- **PlaybackService**: A `MediaSessionService` that manages the `ExoPlayer` instance and `MediaSession`. This ensures playback continues when the UI is not visible.
- **MediaController**: Used by the Compose UI to interact with the `PlaybackSession`.
- **HlsMediaSource**: To handle `.m3u8` streams from Audiobookshelf.

## Handling Authentication in Media3
Since Audiobookshelf requires a Bearer token, we need to configure `DefaultHttpDataSource.Factory` with a custom header provider or use the `token` query parameter for HLS segments.

## Implementation Steps
1. **Add Dependencies**:
   - `androidx.media3:media3-exoplayer`
   - `androidx.media3:media3-session`
   - `androidx.media3:media3-ui`
2. **Create PlaybackService**: Extend `MediaSessionService`.
3. **Initialize ExoPlayer**: Configure with `AudioAttributes` for media playback.
4. **Implement Playback Logic**:
   - Fetch stream URL from ABS API.
   - Set `MediaItem` with the URL and headers.
   - Start playback.
5. **Progress Syncing**:
   - Implement a listener to periodically send `currentTime` to the ABS server.

## WearOS Specifics
- **Ongoing Activity**: Register the `MediaSession` with the system to show a "now playing" indicator on the watch face and in the app launcher.
- **Volume Controls**: Handle hardware buttons or the rotary side button (RSB).
