package com.fair.kotlin_media_player.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.fair.kotlin_media_player.*
import com.fair.kotlin_media_player.adapter.RecordedAudioRecyclerAdapter
import com.fair.kotlin_media_player.databinding.FragmentRecordedAudioBinding
import com.fair.kotlin_media_player.viewmodel.DataTransferViewModel
import com.fair.kotlin_media_player.viewmodel.RecordedAudioViewModel
import com.fair.kotlin_media_player.viewmodel.RecordedAudioViewModelFactory
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance

class RecordedAudioFragment: Fragment(R.layout.fragment_recorded_audio), DIAware {

    override val di by closestDI()
    private val factoryModel: RecordedAudioViewModelFactory by instance()
    private lateinit var viewModel: RecordedAudioViewModel

    private val _viewModel: DataTransferViewModel by activityViewModels()

    private var _binding: FragmentRecordedAudioBinding? = null
    private val viewBinding get() = _binding!!

    private lateinit var _adapter : RecordedAudioRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordedAudioBinding.bind(view)
        viewModel = ViewModelProvider(this, factoryModel).get(RecordedAudioViewModel::class.java)
        _adapter = RecordedAudioRecyclerAdapter(listOf(), _viewModel)


        viewBinding.apply {

            recordedAudioToolbar.apply {
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener {
                    Navigation.findNavController(view).navigate(R.id.action_recordedAudioFragment_to_audioRecorderFragment)
                }
            }

            viewModel.getAllRecorded().observe(viewLifecycleOwner, { recordedList ->

                if (recordedList.isNullOrEmpty()) {

                    recordedAudioRecycler.visibility = View.GONE
                    recordedAudioWarning.visibility = View.VISIBLE

                } else {

                    recordedAudioWarning.visibility = View.GONE
                    recordedAudioRecycler.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = _adapter
                    }

                    _adapter.recordedAudio = recordedList.sortedByDescending { it.b_audioFileTimeStamp }
                    _adapter.notifyDataSetChanged()

                }


            })


        }

    }
}