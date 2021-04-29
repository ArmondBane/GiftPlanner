package com.example.giftplanner.ui.presentslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.data.Entity.Present
import com.example.giftplanner.databinding.PresentsListFragmentBinding
import com.example.giftplanner.util.NpaLinerLayoutManager
import com.example.giftplanner.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PresentsListFragment
    : Fragment(R.layout.presents_list_fragment), PresentsListAdapter.OnItemClickListener {

    private val viewModel: PresentsListViewModel by viewModels()

    private val presentsListAdapter: PresentsListAdapter = PresentsListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PresentsListFragmentBinding.bind(view)

        binding.apply {
            presentsListView.apply {
                adapter = presentsListAdapter
                layoutManager = NpaLinerLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAdd.setOnClickListener {
                viewModel.onAddButtonClick()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.data.observe(viewLifecycleOwner) {
            presentsListAdapter.setData(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is PresentsListViewModel.Event.NavigateToAddPresent -> {

                    }
                    is PresentsListViewModel.Event.NavigateToEditPresent -> {

                    }
                    is PresentsListViewModel.Event.ShowEditConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(present: Present) {
        viewModel.onPresentItemClick(present)
    }
}