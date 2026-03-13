# Audiobookshelf WearOS - Offline Implementation

## Overview
Strategy for implementing offline downloads in the WearOS app.

## Implementation Details
- **Worker**: Use `androidx.work.WorkManager` to handle downloads in the background. This ensures downloads continue even if the app is closed or the watch goes to sleep (when connected to Wi-Fi/LTE).
- **Download Storage**: Store files in `context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)`.
- **Database**: Use Room to track downloaded items, their local file paths, and download status.
- **Media3 Integration**:
  - Use `DownloadManager` from Media3 if possible, as it integrates well with `ExoPlayer`.
  - Provide a `CacheDataSource` to the player so it can play from the local cache if available.

## Syncing
- When a download completes, notify the user.
- Allow the user to "Delete Download" to free up space on the watch.
