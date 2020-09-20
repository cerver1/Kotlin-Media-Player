package com.fair.kotlin_media_player.repository

import com.fair.kotlin_media_player.db.database.RecordedAudioDatabase
import com.fair.kotlin_media_player.db.entity.RecordedAudioEntity

class RecordedAudioRepository(private val database: RecordedAudioDatabase) {

    suspend fun upsert(recordedAudio: RecordedAudioEntity) = database.getRecordedDao().upsert(recordedAudio)

    suspend fun delete(recordedAudio: RecordedAudioEntity) = database.getRecordedDao().delete(recordedAudio)

    fun getAllRecorded() = database.getRecordedDao().getAll()
}