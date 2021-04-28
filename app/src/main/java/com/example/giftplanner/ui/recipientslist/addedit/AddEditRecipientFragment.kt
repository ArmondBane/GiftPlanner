package com.example.giftplanner.ui.recipientslist.addedit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.databinding.EditRecipientFragmentBinding
import com.example.giftplanner.ui.planslist.PlansListViewModel
import com.example.giftplanner.util.exhaustive
import kotlinx.coroutines.flow.collect

class AddEditRecipientFragment : Fragment(R.layout.edit_recipient_fragment) {

    private val viewModel: AddEditRecipientViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditRecipientFragmentBinding.bind(view)

        binding.apply {

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is AddEditRecipientViewModel.Event.NavigateBackWithResult -> TODO()
                }.exhaustive
            }
        }
    }
}