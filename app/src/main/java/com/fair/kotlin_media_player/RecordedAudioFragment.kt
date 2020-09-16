package com.fair.kotlin_media_player

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fair.kotlin_media_player.databinding.FragmentRecordedAudioBinding
import java.io.File

class RecordedAudioFragment: Fragment(R.layout.fragment_recorded_audio) {

    private var _binding: FragmentRecordedAudioBinding? = null
    private val viewBinding get() = _binding!!

    private var pathToFiles: String? = null
    private lateinit var getDirectory: File
    private lateinit var allFiles: List<File>

    private lateinit var _adapter : RecordedAudioRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordedAudioBinding.bind(view)
        pathToFiles = context?.getExternalFilesDir(null)?.absolutePath
        getDirectory = File(pathToFiles.toString())
        allFiles = getDirectory.listFiles().toList()

        _adapter = RecordedAudioRecyclerAdapter(context, allFiles)

        viewBinding.apply {

            recordedAudioRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = _adapter
            }


            /**
             *
             * RecyclerView to display audio files
             *
             *

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