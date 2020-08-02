package com.fair.kotlin_media_player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.fair.kotlin_media_player.databinding.FragmentMusicPlayerBinding

class MusicPlayerFragment: Fragment(R.layout.fragment_music_player) {

    private lateinit var mp: MediaPlayer
    private var totalTime:Int = 0
    private var _binding: FragmentMusicPlayerBinding? = null
    private val viewBinding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMusicPlayerBinding.bind(view)
        mp = MediaPlayer.create(context, R.raw.premonition)
        mp.isLooping = true
        mp.setVolume(0.5f,0.5f)
        totalTime = mp.duration

        viewBinding.apply {

            seekBar.max = totalTime
            seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if(p2) {
                        // research better way of syncing device volume with the apps volume
                        mp.seekTo(p1)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    TODO("Not yet implemented")
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    TODO("Not yet implemented")
                }

            })

            volumeBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                        if(p2) {
                            // research better way of syncing device volume with the apps volume
                            var volumeNumber = p1/ 100.0f
                            mp.setVolume(volumeNumber, volumeNumber)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                        TODO("Not yet implemented")
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                        TODO("Not yet implemented")
                    }

                })

            playFloatingActionButton.setOnClickListener {

                if(mp.isPlaying) {
                    //stop
                    mp.pause()
                    //set the drawable to play
                } else {
                    //start
                    mp.start()
                    //set the drawable to pause
                }

            }
        }

        Thread(Runnable{
            while (mp != null) {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {

                }
            }
        }).start()

    }
    @SuppressLint("HandlerLeak")
    var handler = object  : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            var currentPosition = msg.what

            //update seek
            viewBinding.seekBar.progress = currentPosition

            //update labels
            var currentTime = createTimeLabel(currentPosition)
            viewBinding.currentTime.text = currentTime
            var remainingTime =  createTimeLabel(totalTime - currentPosition)
            viewBinding.currentTime.text = "-$remainingTime"
        }
    }

    fun createTimeLabel(time: Int): String {
        var timelabel = ""
        var min = time/ 100/ 60
        var sec = time / 1000 % 60
        timelabel = "$min"
        if (sec < 10) timelabel += "0"
        timelabel += sec

        return timelabel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}