package com.fair.kotlin_media_player.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.fair.kotlin_media_player.R
import com.fair.kotlin_media_player.databinding.FragmentSettingsContainerBinding

class SettingsContainerFragment: Fragment(R.layout.fragment_settings_container) {

    private var _binding : FragmentSettingsContainerBinding? = null
    private val viewBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsContainerBinding.bind(view)

        viewBinding.apply {

            settingToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            settingToolbar.setNavigationOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_settingsContainerFragment_to_musicPlayerFragment)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.content, MainSettings() )
                .commit()

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}