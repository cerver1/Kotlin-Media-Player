package com.fair.kotlin_media_player

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fair.kotlin_media_player.databinding.FragmentRecordedAudioBinding
import java.io.File

class RecordedAudioFragment: Fragment(R.layout.fragment_recorded_audio) {

    private var _binding: FragmentRecordedAudioBinding? = null
    private val viewBinding get() = _binding!!

    private var pathToFiles: String? = null
    private lateinit var getDirectory: File
    private lateinit var allFiles: Array<File>

    private lateinit var _adapter : RecordedAudioRecyclerAdapter
    private val _model: DataTransferViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordedAudioBinding.bind(view)
        pathToFiles = context?.getExternalFilesDir(null)?.absolutePath
        getDirectory = File(pathToFiles.toString())
        allFiles = getDirectory.listFiles() as Array<File>

        _adapter = RecordedAudioRecyclerAdapter(allFiles.toList(), _model)

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