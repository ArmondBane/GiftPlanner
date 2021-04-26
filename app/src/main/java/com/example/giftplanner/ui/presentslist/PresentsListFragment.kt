package com.example.giftplanner.ui.presentslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.databinding.PresentsListFragmentBinding
import com.example.giftplanner.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PresentsListFragment : Fragment(R.layout.presents_list_fragment) {

    private val viewModel: PresentsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PresentsListFragmentBinding.bind(view)

        binding.apply {

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {

                    else -> {}
                }.exhaustive
            }
        }
    }
}