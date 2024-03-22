package edu.oregonstate.cs492.assignment4.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey @ColumnInfo(name = "shareUrl") val shareUrl: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "songTitle") val songTitle: String,
    @ColumnInfo(name = "shortUrl") val shortUrl: String,
    @ColumnInfo(name = "songImage") val songImage: String,
    @ColumnInfo(name = "duration") val duration: Int
)

