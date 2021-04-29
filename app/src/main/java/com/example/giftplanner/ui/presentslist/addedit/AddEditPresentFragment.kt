package com.example.giftplanner.ui.presentslist.addedit

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftplanner.R
import com.example.giftplanner.databinding.EditPresentFragmentBinding
import com.example.giftplanner.ui.planslist.PlansListViewModel
import com.example.giftplanner.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class AddEditPresentFragment : Fragment(R.layout.edit_present_fragment) {

    private val viewModel: AddEditPresentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditPresentFragmentBinding.bind(view)

        binding.apply {

            nameView.setText(viewModel.name)
            nameView.addTextChangedListener {
                viewModel.name = it.toString()
            }

            costView.setText(viewModel.cost.toString())
            costView.addTextChangedListener {
                viewModel.cost = it.toString().toDouble()
            }

            addFAB.setOnClickListener {
                viewModel.onConfirmButtonClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is AddEditPresentViewModel.Event.NavigateBackWithResult -> {
                        setFragmentResult("add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditPresentViewModel.Event.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        if (viewModel.isDeleteAllow)
            setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.present_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_item -> {
                makeDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Вы уверены, что хотите удалить подарок?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.onDeleteMenuClick()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
        val alert = dialogBuilder.create()
        alert.show()
    }
}