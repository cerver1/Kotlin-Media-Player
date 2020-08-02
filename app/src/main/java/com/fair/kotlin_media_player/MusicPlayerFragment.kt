package com.fair.kotlin_media_player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusicPlayerBinding.bind(view)

        mp = MediaPlayer.create(context, R.raw.premonition)
        mp?.isLooping = true
        mp?.setVolume(0.5f,0.5f)
        totalTime = mp?.duration

        viewBinding.apply {
            seekBar.max = totalTime!!
            currentTime.text = "0 : 00"
            remainingTime.text = "-${createTimeLabel(totalTime)}"

            seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, progress: Int, change: Boolean) {
                    if(change) {
                        // research better way of syncing device volume with the apps volume
                        mp?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })
            volumeBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if(p2) {
                            // research better way of syncing device volume with the apps volume
                            val volumeNumber = p1/ 100.0f
                            mp?.setVolume(volumeNumber, volumeNumber)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }

                })

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}