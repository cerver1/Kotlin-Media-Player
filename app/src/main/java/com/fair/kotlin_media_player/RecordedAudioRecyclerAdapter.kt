package com.fair.kotlin_media_player

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_recorded_audio.view.*
import java.io.File

class RecordedAudioRecyclerAdapter(var recordedAudio: List<RecordedAudioEntity>,
                                   private var model: DataTransferViewModel):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_recorded_audio, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = recordedAudio.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            recyclerAudioFileName.text = recordedAudio[position].a_audioFileName
            recyclerAudioFileModified.text = timeAgo(recordedAudio[position].b_audioFileTimeStamp)

            setOnClickListener {

                model.apply {
                    audioFile.value = recordedAudio[position].c_audioFile
                    audioFileName.value = recordedAudio[position].a_audioFileName
                    audioFileTimeStamp.value = recordedAudio[position].b_audioFileTimeStamp

                }



                findNavController().navigate(R.id.action_recordedAudioFragment_to_musicPlayerFragment)

            }
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}