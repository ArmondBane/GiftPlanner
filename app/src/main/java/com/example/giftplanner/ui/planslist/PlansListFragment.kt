package com.example.giftplanner.ui.planslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.databinding.PlansListFragmentBinding
import com.example.giftplanner.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PlansListFragment : Fragment(R.layout.plans_list_fragment) {

    private val viewModel: PlansListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PlansListFragmentBinding.bind(view)

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