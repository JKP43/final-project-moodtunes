package edu.oregonstate.cs492.assignment4.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SongEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}
