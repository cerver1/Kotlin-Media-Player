package com.fair.kotlin_media_player

class RecordedAudioRepository(private val database: RecordedAudioDatabase) {

    suspend fun upsert(recordedAudio: RecordedAudioEntity) = database.getRecordedDao().upsert(recordedAudio)

    suspend fun delete(recordedAudio: RecordedAudioEntity) = database.getRecordedDao().delete(recordedAudio)

    fun getAllRecorded() = database.getRecordedDao().getAll()
}