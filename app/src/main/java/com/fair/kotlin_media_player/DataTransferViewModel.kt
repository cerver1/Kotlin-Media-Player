package com.fair.kotlin_media_player

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class DataTransferViewModel: ViewModel() {

    val audioFileName = MutableLiveData<String>()
    val audioFileTimeStamp = MutableLiveData<String>()
    val audioFile = MutableLiveData<File>()
}