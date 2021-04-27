package com.example.giftplanner.ui.planslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.giftplanner.R
import com.example.giftplanner.data.Entity.Plan
import com.example.giftplanner.databinding.PlansListFragmentBinding
import com.example.giftplanner.util.NpaLinerLayoutManager
import com.example.giftplanner.util.exhaustive
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

        viewModel.data.observe(viewLifecycleOwner) {
            plansListAdapter.setData(it)
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

    override fun onItemClick(plan: Plan) {
        viewModel.onPlanItemClick(plan)
    }
}