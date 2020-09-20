package com.fair.kotlin_media_player.viewmodel

import androidx.lifecycle.ViewModel
import com.fair.kotlin_media_player.db.entity.RecordedAudioEntity
import com.fair.kotlin_media_player.repository.RecordedAudioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordedAudioViewModel(private val repository: RecordedAudioRepository): ViewModel() {

    fun upsert(recordAudio: RecordedAudioEntity) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(recordAudio)
    }

    fun delete(recordAudio: RecordedAudioEntity) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(recordAudio)
    }

    fun getAllRecorded() = repository.getAllRecorded()

}