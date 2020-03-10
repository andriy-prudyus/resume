package com.andriiprudyus.myresume.ui.company.details.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriiprudyus.myresume.MainActivity
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.viewModel.ResultObserver
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.extension.showErrorSnackbar
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.viewModel.CompanyDetailsViewModel
import kotlinx.android.synthetic.main.fragment_company_details.*
import javax.inject.Inject

class CompanyDetailsFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(R.layout.fragment_company_details) {

    private val viewModel by viewModels<CompanyDetailsViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (arguments?.let { CompanyDetailsFragmentArgs.fromBundle(it).companyName } ?: "").let {
            viewModel.companyName = it
            (activity as? MainActivity)?.supportActionBar?.title = it
        }

        loadData()
    }

    private fun initControls() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RolesAdapter()
        }
    }

    private fun loadData() {
        viewModel.items.also { liveData ->
            liveData.observe(viewLifecycleOwner, ResultObserver(liveData) { state ->
                when (state) {
                    is State.Success -> {
                        progressBar.isVisible = false

                        if (state.data.isEmpty()) {
                            noDataTextView.isVisible = true
                            recyclerView.isVisible = false
                        } else {
                            noDataTextView.isVisible = false
                            recyclerView.isVisible = true
                            (recyclerView.adapter as RolesAdapter).replaceItems(state.data)
                        }
                    }
                    is State.Failure -> {
                        progressBar.isVisible = false
                        noDataTextView.isVisible = true
                        view?.let { showErrorSnackbar(it, state.throwable) }
                    }
                }
            })
        }
    }
}