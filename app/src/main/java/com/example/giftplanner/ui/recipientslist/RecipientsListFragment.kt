package com.example.giftplanner.ui.recipientslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.databinding.RecipientsListFragmentBinding
import com.example.giftplanner.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipientsListFragment : Fragment(R.layout.recipients_list_fragment){

    private val viewModel: RecipientsListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = RecipientsListFragmentBinding.bind(view)

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