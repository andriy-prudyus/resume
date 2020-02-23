package com.andriiprudyus.myresume.ui.company.details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriiprudyus.myresume.MainActivity
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.viewModel.ResultObserver
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.viewModel.CompanyDetailsViewModel
import com.andriiprudyus.myresume.ui.company.details.viewModel.CompanyDetailsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_company_details.*

class CompanyDetailsFragment : Fragment() {

    private val viewModel by viewModels<CompanyDetailsViewModel>(
        { this },
        {
            CompanyDetailsViewModelFactory(
                arguments?.let { CompanyDetailsFragmentArgs.fromBundle(it).companyName } ?: ""
            )
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = viewModel.companyName
        loadData()
    }

    private fun initControls() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RolesAdapter()
        }
    }

    private fun loadData() {
        viewModel.getItems().also { liveData ->
            liveData.observe(viewLifecycleOwner, ResultObserver(liveData) { state ->
                when (state) {
                    is State.Success -> {
                        progressBar.isVisible = false
                        noDataTextView.isVisible = false
                        recyclerView.isVisible = true
                        (recyclerView.adapter as RolesAdapter).replaceItems(state.data)
                    }
                    is State.Failure -> {
                        progressBar.isVisible = false
                        noDataTextView.isVisible = true

                        view?.let {
                            Snackbar
                                .make(it, state.throwable.localizedMessage, Snackbar.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            })
        }
    }
}