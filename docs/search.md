# Audiobookshelf WearOS - Search Implementation

## Overview
Strategy for implementing search (books and series) on WearOS.

## Implementation Details
- **UI**: Use a `SearchScreen` with a `TextField` for input.
- **On-Watch Keyboard**: Support the system IME.
- **Voice Search**: Use `RemoteInput` or integrated voice-to-text if available on the watch.
- **API Endpoints**:
  - `GET /api/libraries/{id}/items?filter={query}`
  - `GET /api/libraries/{id}/series?filter={query}`

## Local Search
- If the library is synced to the local Room DB, perform a `LIKE` query on the `library_items` table for fast offline results.
- `SELECT * FROM library_items WHERE title LIKE :query OR author LIKE :query`
- **Implemented**: `LibraryDao.searchBooks(query)` and `LibraryDao.searchSeries(query)` are scaffolded in `AppDatabase.kt`.

## Result Display
- Display results in a `ScalingLazyColumn`.
- Show book cover, title, and author.
- Clicking an item navigates to the `BookDetailsScreen`.
