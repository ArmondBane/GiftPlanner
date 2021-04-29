package com.example.giftplanner.ui.recipientslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftplanner.R
import com.example.giftplanner.data.Entity.Recipient
import com.example.giftplanner.databinding.RecipientsListFragmentBinding
import com.example.giftplanner.util.NpaLinerLayoutManager
import com.example.giftplanner.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipientsListFragment
    : Fragment(R.layout.recipients_list_fragment), RecipientsListAdapter.OnItemClickListener {

    private val viewModel: RecipientsListViewModel by viewModels()

    private val recipientsListAdapter: RecipientsListAdapter = RecipientsListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = RecipientsListFragmentBinding.bind(view)

        binding.apply {
            recipientsListView.apply {
                adapter = recipientsListAdapter
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
            recipientsListAdapter.setData(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is RecipientsListViewModel.Event.NavigateToAddRecipient -> {
                        val action = RecipientsListFragmentDirections
                                        .actionRecipientsListFragmentToAddEditRecipientFragment(
                                                null,
                                                "Добавление получателя"
                                        )
                        findNavController().navigate(action)
                    }
                    is RecipientsListViewModel.Event.NavigateToEditRecipient -> {
                        val action = RecipientsListFragmentDirections
                                        .actionRecipientsListFragmentToAddEditRecipientFragment(
                                                event.recipient,
                                                "Редактирование получателя"
                                        )
                        findNavController().navigate(action)
                    }
                    is RecipientsListViewModel.Event.ShowEditConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(recipient: Recipient) {
        viewModel.onRecipientItemClick(recipient)
    }
}