package com.fair.kotlin_media_player

import android.Manifest

val permissions = arrayOf(
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

const val REQUEST_CODE = 1020
