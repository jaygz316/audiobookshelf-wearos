package com.jaybit.audiobookshelf

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_items")
data class LibraryItem(
    @PrimaryKey val id: String,
    val title: String,
    val author: String?,
    val coverUrl: String?,
    val duration: Double,
    val currentTime: Double,
    val isFinished: Boolean,
    val series: String? = null,
    val localPath: String? = null
)
