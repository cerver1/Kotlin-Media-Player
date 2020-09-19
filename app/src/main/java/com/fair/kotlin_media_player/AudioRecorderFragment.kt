package com.fair.kotlin_media_player

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fair.kotlin_media_player.databinding.FragmentAudioRecorderBinding
import com.google.android.material.snackbar.Snackbar
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AudioRecorderFragment:Fragment(R.layout.fragment_audio_recorder), DIAware {


    override val di by closestDI()
    private val factoryModel: RecordedAudioViewModelFactory by instance()
    private lateinit var viewModel: RecordedAudioViewModel

    private var _binding: FragmentAudioRecorderBinding? = null
    private val viewBinding get() = _binding!!

    private var mediaRecorder: MediaRecorder? = null

    private var file : String? = null
    private var filePath: String? = null
    private var fileExtension: String? = null

    private lateinit var formatter: SimpleDateFormat
    private lateinit var now: Date

    private var isRecording = false

    private lateinit var timer: Chronometer
    private lateinit var _timer: Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAudioRecorderBinding.bind(view)
        viewModel = ViewModelProvider(this, factoryModel).get(RecordedAudioViewModel::class.java)


        viewBinding.apply {


            timer = counterDisplay

            file = context?.getExternalFilesDir(null)?.absolutePath
            formatter = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US)
            now = Date()
            fileExtension = "/recording"+ formatter.format(now) +".mp3"

            filePath = file + fileExtension

            recordFloatingActionButton.setOnClickListener {
                if (ContextCompat.checkSelfPermission( requireContext(), RECORD_AUDIO) !=
                   PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
                        WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_CODE)
                } else {
                    audioRecorder()
                }
            }
            libraryFloatingActionButton.setOnClickListener {
                Navigation.findNavController(view).navigate(R.id.action_audioRecorderFragment_to_recordedAudioFragment)
            }
            saveFloatingActionButton.setOnClickListener {
                Snackbar.make(view, "Coming soon", Snackbar.LENGTH_SHORT).show()
            }


        }
    }


    private fun startRecording() {
        timer.base = SystemClock.elapsedRealtime()
        timer.start()

        viewBinding.audioPlayerCircleVisualizerView.recreate()
        _timer = Timer()
        _timer.schedule(object : TimerTask() {
            override fun run() {
                mediaRecorder?.maxAmplitude?.let { amp ->
                    viewBinding.audioPlayerCircleVisualizerView.update(amp)
                }
            }
        }, 0, 100)

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(filePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            viewBinding.audioFileName.text = "$filePath"

            try {
               prepare()
            } catch (e: IOException) {
                // unable to prepare
            }

            start()
            isRecording = true
        }

    }


    private fun audioRecorder() {
        if(isRecording) {
            viewBinding.recordFloatingActionButton.setImageResource(R.drawable.ic_recording_stopped)
            stopRecording()
        } else {
            viewBinding.recordFloatingActionButton.setImageResource(R.drawable.ic_recording_started)
            startRecording()
        }
    }

    private fun stopRecording() {
        timer.stop()
        _timer.cancel()

        mediaRecorder?.apply {

            stop()
            release()

        }

        mediaRecorder = null
        isRecording = false
        viewModel.upsert(
            RecordedAudioEntity(
                //TODO change to dynamic name dialog

                a_audioFileName = "Change to dynamic name dialog",
                b_audioFileTimeStamp = File(filePath.toString()).lastModified(),
                c_audioFile = filePath.toString()
            )
        )

    }


    //TODO IF THE USER DOESN'T manually save do if for them automatically.

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.audioPlayerCircleVisualizerView.recreate()
        _binding = null
    }
}