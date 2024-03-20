package edu.oregonstate.cs492.assignment4.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(songEntity: SongEntity)

    @Query("SELECT * FROM songs")
    suspend fun getAllSongs(): List<SongEntity>
}
