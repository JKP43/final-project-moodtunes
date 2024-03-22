package edu.oregonstate.cs492.assignment4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(songEntity: SongEntity) : Long

    @Query("SELECT * FROM songs")
    suspend fun getAllSongs(): List<SongEntity>
    @Query("DELETE FROM songs WHERE shareUrl = :shareUrl")
    suspend fun deleteSong(shareUrl: String)
}
