package com.fair.kotlin_media_player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fair.kotlin_media_player.repository.RecordedAudioRepository

class RecordedAudioViewModelFactory(private val repository: RecordedAudioRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecordedAudioViewModel(repository) as T
    }
}