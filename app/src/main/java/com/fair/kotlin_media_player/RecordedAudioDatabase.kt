package com.fair.kotlin_media_player

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecordedAudioEntity::class], version = 1, exportSchema = false)
abstract class RecordedAudioDatabase: RoomDatabase() {
    abstract fun getRecordedDao() : RecordedAudioDao

    companion object {
        @Volatile
        private var INSTANCE: RecordedAudioDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, RecordedAudioDatabase::class.java,
                "cerve_audio_database.db")
                .fallbackToDestructiveMigration()
                .build()

    }
}