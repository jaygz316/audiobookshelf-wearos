package com.jaybit.audiobookshelf

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Database
import androidx.room.RoomDatabase

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library_items")
    suspend fun getAll(): List<LibraryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LibraryItem>)

    @Query("UPDATE library_items SET currentTime = :progress WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Double)

    @Query("SELECT * FROM library_items WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    suspend fun searchBooks(query: String): List<LibraryItem>

    @Query("SELECT DISTINCT series FROM library_items WHERE series LIKE '%' || :query || '%'")
    suspend fun searchSeries(query: String): List<String?>
}

@Database(entities = [LibraryItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
}
