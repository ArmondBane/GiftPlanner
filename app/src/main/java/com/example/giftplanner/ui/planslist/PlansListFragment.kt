package com.example.giftplanner.ui.planslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.giftplanner.R
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.databinding.PlansListFragmentBinding
import com.example.giftplanner.util.NpaLinerLayoutManager
import com.example.giftplanner.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PlansListFragment
    : Fragment(R.layout.plans_list_fragment), PlansListAdapter.OnItemClickListener {

    private val viewModel: PlansListViewModel by viewModels()

    private val plansListAdapter: PlansListAdapter = PlansListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PlansListFragmentBinding.bind(view)

        binding.apply {
            plansListView.apply {
                adapter = plansListAdapter
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
            plansListAdapter.setData(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.event.collect { event ->
                when (event) {
                    is PlansListViewModel.Event.NavigateToAddPlan -> {
                        val action = PlansListFragmentDirections
                                        .actionPlansListFragmentToAddEditPlanFragment(
                                                null,
                                                "Добавление плана"
                                        )
                        findNavController().navigate(action)
                    }
                    is PlansListViewModel.Event.NavigateToEditPlan -> {
                        val action = PlansListFragmentDirections
                                        .actionPlansListFragmentToAddEditPlanFragment(
                                                event.plan,
                                                "Редактирование плана"
                                        )
                        findNavController().navigate(action)
                    }
                    is PlansListViewModel.Event.ShowEditConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }

    override fun onItemClick(plan: Plan) {
        viewModel.onPlanItemClick(plan)
    }
}