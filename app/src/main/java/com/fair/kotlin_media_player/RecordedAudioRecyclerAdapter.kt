package com.fair.kotlin_media_player

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_recorded_audio.view.*
import java.io.File

class RecordedAudioRecyclerAdapter(val context: Context?, var recordedAudio: List<File>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_recorded_audio, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount( )= recordedAudio.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            recyclerAudioFileName.text = recordedAudio[position].toString()
        }
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}