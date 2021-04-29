package com.example.giftplanner.ui.planslist.addedit

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftplanner.R
import com.example.giftplanner.databinding.EditPlanFragmentBinding
import com.example.giftplanner.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Suppress("IMPLICIT_CAST_TO_ANY")
@AndroidEntryPoint
class AddEditPlanFragment: Fragment(R.layout.edit_plan_fragment) {

    private val viewModel: AddEditPlanViewModel by viewModels()

    private val calendar = Calendar.getInstance()

    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditPlanFragmentBinding.bind(view)

        binding.apply {

            holidayNameView.setText(viewModel.holidayName)
            holidayNameView.addTextChangedListener {
                viewModel.holidayName = it.toString()
            }

            dateView.text = viewModel.dateString
            dateView.setOnClickListener {
                DatePickerDialog(requireContext(), dateSetListener,
                    viewModel.dateYear,
                    viewModel.dateMonth,
                    viewModel.dateDay).show()
            }

            val presentSpinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.presentList
            )
            presentSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            presentSpinner.apply {
                adapter = presentSpinnerAdapter
                setSelection(viewModel.presentId-1)
            }
            presentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?,
                    selectedItemPosition: Int,
                    selectedId: Long
                ) {
                    if(itemSelected != null) {
                        viewModel.presentId = selectedItemPosition+1
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            val recipientSpinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.recipientList
            )
            recipientSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            recipientSpinner.apply {
                adapter = recipientSpinnerAdapter
                setSelection(viewModel.recipientId-1)
            }
            recipientSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?,
                    selectedItemPosition: Int,
                    selectedId: Long
                ) {
                    if(itemSelected != null) {
                        viewModel.recipientId = selectedItemPosition+1
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            addFAB.setOnClickListener {
                viewModel.onConfirmButtonClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is AddEditPlanViewModel.Event.ChangeDateView -> {
                        binding.dateView.text = viewModel.dateString
                    }
                    is AddEditPlanViewModel.Event.NavigateBackWithResult -> {
                        setFragmentResult("add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                    is AddEditPlanViewModel.Event.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        if (viewModel.isDeleteAllow)
            setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.plan_edit_menu, menu)
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
        dialogBuilder.setMessage("Вы уверены, что хотите удалить план?")
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

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        viewModel.dateString = sdf.format(calendar.time)
        viewModel.dateYear = year
        viewModel.dateMonth = monthOfYear
        viewModel.dateDay = dayOfMonth
        viewModel.dateLong =
            LocalDateTime.of(year, monthOfYear+1, dayOfMonth, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        viewModel.onDateChanged()
    }
}