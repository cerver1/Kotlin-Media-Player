@file:Suppress("unused")

package com.fair.kotlin_media_player.di

import android.app.Application
import com.fair.kotlin_media_player.db.database.RecordedAudioDatabase
import com.fair.kotlin_media_player.repository.RecordedAudioRepository
import com.fair.kotlin_media_player.viewmodel.RecordedAudioViewModelFactory
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class MainApplication: Application(), DIAware {

    override val di: DI
        get() = DI.lazy {
            import(androidXModule(this@MainApplication))
            bind() from singleton { RecordedAudioDatabase(instance()) }
            bind() from singleton { RecordedAudioRepository(instance()) }
            bind() from singleton { RecordedAudioViewModelFactory(instance()) }

        }
}