package com.fair.kotlin_media_player

import android.Manifest
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.AudioManager
import android.media.AudioManager.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.fair.kotlin_media_player.databinding.FragmentMusicPlayerBinding
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MusicPlayerFragment: Fragment(R.layout.fragment_music_player) {

    private var mp: MediaPlayer? = null
    private var totalTime: Int? = 0
    private var _binding: FragmentMusicPlayerBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var audio : AudioManager
    private var visualID : Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusicPlayerBinding.bind(view)

        audio = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        if (checkSelfPermission(requireContext(), RECORD_AUDIO) != PERMISSION_GRANTED){
                val permissions = arrayOf(RECORD_AUDIO)
                requestPermissions(permissions,0)
        }



        mp = MediaPlayer.create(context, R.raw.premonition)
        mp?.isLooping = true
        mp?.setVolume(0.5f,0.5f)
        totalTime = mp?.duration
        visualID = mp?.audioSessionId

        viewBinding.apply {
            seekBar.max = totalTime!!
            currentTime.text = "0 : 00"
            remainingTime.text = "-${createTimeLabel(totalTime)}"
            volumeBar.max = audio.getStreamMaxVolume(STREAM_MUSIC)
            volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)

            if (visualID != -1) {
                visualID?.let {
                    circleVisualizerView.isDrawLine = true
                    circleVisualizerView.setAudioSessionId(it) }
            }

            seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, change: Boolean) {
                    if(change) {
                        mp?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
            volumeUp.apply {

                setOnClickListener {
                    audio.adjustStreamVolume(STREAM_MUSIC, ADJUST_RAISE, FLAG_PLAY_SOUND)
                    volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)
                }

            }
            volumeDown.setOnClickListener {
                audio.adjustStreamVolume(STREAM_MUSIC, ADJUST_LOWER, FLAG_PLAY_SOUND)
                volumeBar.progress = audio.getStreamVolume(STREAM_MUSIC)
            }

            playFloatingActionButton.setOnClickListener {

                    if(mp?.isPlaying!!) stop() else play()


            }
        }




    }

    private fun play(){
        mp?.start()
        viewBinding.playFloatingActionButton.setImageResource(R.drawable.ic_pause)
        CoroutineScope(Dispatchers.Main).launch {

            while (mp?.isPlaying!!) {
                viewBinding.apply {
                    val location = mp?.currentPosition!!
                    currentTime.text = createTimeLabel(location)
                    val remaining = createTimeLabel(totalTime?.minus(location))
                    remainingTime.text = "-$remaining"
                    seekBar.progress = location
                    delay(100)
                }
            }
        }
    }
    private fun stop(){
        viewBinding.playFloatingActionButton.setImageResource(R.drawable.ic_play)
        mp?.pause()
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
    fun AudioManager.setMediaVolume(volumeIndex: Int) {
        this.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            volumeIndex,
            AudioManager.FLAG_SHOW_UI
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.circleVisualizerView.release()
        _binding = null

    }
}