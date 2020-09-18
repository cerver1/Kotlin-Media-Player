package com.fair.kotlin_media_player

import android.Manifest.permission.*
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.AudioManager
import android.media.AudioManager.*
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fair.kotlin_media_player.databinding.FragmentMusicPlayerBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class MusicPlayerFragment: Fragment(R.layout.fragment_music_player) {

    private var _binding: FragmentMusicPlayerBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var mp: MediaPlayer
    private var totalTime: Int? = 0

    private lateinit var audio : AudioManager
    private var visualID : Int? = null

    private val _model: DataTransferViewModel by activityViewModels()
    private var fileToPlay: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusicPlayerBinding.bind(view)

        audio = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        fileToPlay = _model.audioFile.value

        if (checkSelfPermission(requireContext(), RECORD_AUDIO) != PERMISSION_GRANTED){
                requestPermissions(permissions, REQUEST_CODE)
        }



        // music player settings

            try {
                mp = MediaPlayer().apply {
                    setDataSource(fileToPlay?.absolutePath)
                    prepare()
                    isLooping = true
                }
            } catch (e: Exception) {
                Snackbar.make(view, "Unable to play file", Snackbar.LENGTH_SHORT).show()
                mp = MediaPlayer.create(context, R.raw.premonition)
                mp.isLooping = true
            }



        totalTime = mp.duration

        // audio visualizer
        visualID = mp.audioSessionId

        viewBinding.apply {

            musicPlayerToolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener {
                    Navigation.findNavController(view).navigate(R.id.action_musicPlayerFragment_to_recordedAudioFragment)
                }
            }

            // audio tracking
            seekBar.max = totalTime!!
            currentTime.text = getString(R.string.startingTime)
            remainingTime.text = getString(R.string.endingTime, createTimeLabel(totalTime))

            // volume tracking
            volumeBar.max = audio.getStreamMaxVolume(STREAM_MUSIC)
            volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)

            // volume controls
            volumeUp.setOnClickListener {
                audio.adjustStreamVolume(STREAM_MUSIC, ADJUST_RAISE, FLAG_PLAY_SOUND)
                volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)
            }
            volumeDown.setOnClickListener {
                audio.adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, FLAG_PLAY_SOUND)
                volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)
            }

            // visualizer tracking
            if (visualID != -1) {
                visualID?.let {
                    Log.d("THISTag", it.toString())
                    circleVisualizerView.isDrawLine = true
                    circleVisualizerView.setAudioSessionId(it) }
            }

            // progress tracking
            seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, change: Boolean) {
                    if(change) {
                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })

            backFloatingActionButton.setOnClickListener {
                backThirtySeconds()
            }
            playFloatingActionButton.setOnClickListener {

                    if(mp.isPlaying) stop() else play()


            }
            forwardFloatingActionButton.setOnClickListener {
                forwardThirtySeconds()
            }

            // navigate to the settings page
            musicPlayerToolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_settings -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_musicPlayerFragment_to_settingsContainerFragment)
                        true }

                    else -> super.onOptionsItemSelected(item)
                }
            }
        }
    }

    private fun backThirtySeconds() {
        CoroutineScope(Dispatchers.Main).launch {
            if (mp.isPlaying) {
                mp.currentPosition.minus(30000).let { mp.seekTo(it) }
            }
        }

    }
    private fun forwardThirtySeconds() {
        CoroutineScope(Dispatchers.Main).launch {
            if (mp.isPlaying) {
                mp.currentPosition.plus(30000).let { mp.seekTo(it) }
            }
        }
    }

    private fun play(){
        mp.start()
        viewBinding.playFloatingActionButton.setImageResource(R.drawable.ic_pause)
        CoroutineScope(Dispatchers.Main).launch {

            while (mp.isPlaying) {
                viewBinding.apply {
                    val location = mp.currentPosition
                    currentTime.text = createTimeLabel(location)
                    val remaining = createTimeLabel(totalTime?.minus(location))
                    remainingTime.text = getString(R.string.remainingTime, remaining)
                    seekBar.progress = location
                    delay(100)
                }
            }
        }
    }
    private fun stop(){
        viewBinding.playFloatingActionButton.setImageResource(R.drawable.ic_play)
        mp.pause()
    }

    private fun createTimeLabel(time: Int?): String {
        val min = (time?.div(1000)?.div(60))
        val sec = (time?.div(1000))?.rem(60)
        val secs = if (sec.toString().length == 1) "0$sec" else sec.toString()
        return if (sec != null) {
            if (sec < 1) "$min : 00"
            else "$min : $secs"
        } else "0 : 00"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // prevents an exception from being thrown on case that the view is destroyed while the music player, plays
        stop()
        viewBinding.circleVisualizerView.release()
        _binding = null

    }
}