package com.fair.kotlin_media_player.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fair.kotlin_media_player.db.entity.RecordedAudioEntity

@Dao
interface RecordedAudioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(audioFile: RecordedAudioEntity)

    @Delete
    suspend fun delete(audioFile: RecordedAudioEntity)

    @Query("SELECT * FROM recorded_audio")
    fun getAll(): LiveData<List<RecordedAudioEntity>>
}