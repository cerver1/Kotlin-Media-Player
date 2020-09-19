@file:Suppress("unused")

package com.fair.kotlin_media_player

import android.app.Application
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