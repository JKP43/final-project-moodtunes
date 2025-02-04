package edu.oregonstate.cs492.assignment4.data

import android.content.Context
import androidx.room.Room

//added database here
object DatabaseBuilder {
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "moodtunes-database"


                )
                .fallbackToDestructiveMigration()
                .build()
            }
        }
        return INSTANCE!!
    }
}
