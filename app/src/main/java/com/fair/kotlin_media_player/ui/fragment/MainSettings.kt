package com.fair.kotlin_media_player.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.fair.kotlin_media_player.R

class MainSettings: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)

    }

}