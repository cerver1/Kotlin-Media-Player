package com.fair.kotlin_media_player

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class MainSettings: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)

    }

}