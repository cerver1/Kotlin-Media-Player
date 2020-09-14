package com.fair.kotlin_media_player

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fair.kotlin_media_player.databinding.FragmentAudioRecorderBinding
import com.fair.kotlin_media_player.databinding.FragmentMusicPlayerBinding
import java.io.IOException

class AudioRecorderFragment:Fragment(R.layout.fragment_audio_recorder) {

    private var _binding: FragmentAudioRecorderBinding? = null
    private val viewBinding get() = _binding!!

    private var output: String? = null
    private var mediaRecorder: MediaRecorder? = null
    private var state: Boolean = false
    private var recordingStopped: Boolean = false

    private var isRecording = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAudioRecorderBinding.bind(view)

        viewBinding.apply {



            output = activity?.getExternalFilesDir("/")?.absolutePath
            // add a simple date format for logging unique files
            //Environment.getExternalStorageDirectory().absolutePath + "/"

            // move initialization to on startRecording
            mediaRecorder = MediaRecorder()
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediaRecorder?.setOutputFile(output + "/" + "recording.3gp")

            buttonStartRecording.setOnClickListener {
                if (ContextCompat.checkSelfPermission( requireContext(),
                       RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireContext(),
                        WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    val permissions = arrayOf(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                    ActivityCompat.requestPermissions(requireActivity(), permissions,0)
                } else {
                    startRecording()
                }
            }

            buttonStopRecording.setOnClickListener{
                stopRecording()
            }

            buttonPauseRecording.setOnClickListener {
                pauseRecording()
            }

        }
    }

    private fun startRecording() {
        try {
            // start timer with possibly chronometer
            // timer.setBase(System.elapseRealtime())
            // timer.start()
            mediaRecorder?.prepare()
            // may need to move outside of try
            mediaRecorder?.start()
            state = true
            Toast.makeText(context, "Recording started!", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        if(state) {
            if(!recordingStopped){
                Toast.makeText(context,"Stopped!", Toast.LENGTH_SHORT).show()
                mediaRecorder?.pause()
                recordingStopped = true
                viewBinding.buttonPauseRecording.text = "Resume"
            }else{
                resumeRecording()
            }
        }
    }

    @SuppressLint("RestrictedApi", "SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {

        Toast.makeText(context,"Resume!", Toast.LENGTH_SHORT).show()
        mediaRecorder?.resume()
        viewBinding.buttonPauseRecording.text = "Pause"
        recordingStopped = false
    }

    private fun stopRecording(){
        // start timer with possibly chronometer
        // also a chronometer object
        // timer.start()
        if(state){
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            state = false
        }else{
            Toast.makeText(context, "You are not recording right now!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClick() {
        if (isRecording){
            isRecording = false
        } else {
            isRecording = true
        }
    }


    /**
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}