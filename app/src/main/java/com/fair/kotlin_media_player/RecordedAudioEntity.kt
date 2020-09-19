package com.fair.kotlin_media_player

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recorded_audio")
data class RecordedAudioEntity (

    @ColumnInfo(name = "audioFileName") var a_audioFileName : String?,
    @ColumnInfo(name = "audioFileTimeStamp") var b_audioFileTimeStamp: Long?,
    @PrimaryKey @NonNull val c_audioFile: String

)