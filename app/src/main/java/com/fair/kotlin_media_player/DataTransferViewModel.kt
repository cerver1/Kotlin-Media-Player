package com.fair.kotlin_media_player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataTransferViewModel: ViewModel() {

    val audioFileName = MutableLiveData<String>()
    val audioFileTimeStamp = MutableLiveData<Long>()
    val audioFile = MutableLiveData<String>()

}