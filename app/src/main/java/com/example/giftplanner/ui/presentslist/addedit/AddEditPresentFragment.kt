package com.example.giftplanner.ui.presentslist.addedit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.databinding.EditPresentFragmentBinding
import com.example.giftplanner.ui.planslist.PlansListViewModel
import com.example.giftplanner.util.exhaustive
import kotlinx.coroutines.flow.collect

class AddEditPresentFragment : Fragment(R.layout.edit_present_fragment) {

    private val viewModel: AddEditPresentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditPresentFragmentBinding.bind(view)

        binding.apply {

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is PlansListViewModel.Event.NavigateToAddPlan -> {

                    }
                    is PlansListViewModel.Event.NavigateToEditPlan -> {

                    }
                }.exhaustive
            }
        }
    }
}