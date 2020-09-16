package com.fair.kotlin_media_player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fair.kotlin_media_player.databinding.FragmentRecordedAudioBinding

class RecordedAudioFragment: Fragment(R.layout.fragment_recorded_audio) {

    private var _binding: FragmentRecordedAudioBinding? = null
    private val viewBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordedAudioBinding.bind(view)

        viewBinding.apply {

            /**
             *
             * RecyclerView to display audio files
             *
             * pathToFiles = activity.getExternalFilesDir("/").getAbsolutePath();
             * getDirectory = File(path)
             * allFiles = getDirectory.listFiles()
             *
             * onClickListener for Holder within recycler adapter to send the audio to the mediaPlayer
             * viewModel to store and transfer files
             *
             * AudioListAdapter(allFiles)
             * audioList.setFixedSize(true)
             *
             * // to retrieve file
             * place within a try catch
             * mediaPlayer.setDataSource(fileToPlay.getAbsolutePath())
             *
             * onStop()
             * for preventing lifecycle conflicts
             *
             */

        }

    }
}