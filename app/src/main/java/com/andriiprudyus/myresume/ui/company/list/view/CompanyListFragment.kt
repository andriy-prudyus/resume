package com.andriiprudyus.myresume.ui.company.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriiprudyus.myresume.R
import com.andriiprudyus.myresume.base.adapter.ItemsDecoration
import com.andriiprudyus.myresume.base.viewModel.ResultObserver
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.db.company.Company
import com.andriiprudyus.myresume.ui.company.list.adapter.CompaniesAdapter
import com.andriiprudyus.myresume.ui.company.list.viewModel.CompanyListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_company_list.*

class CompanyListFragment : Fragment(), CompaniesAdapter.ActionListener {

    private val viewModel by viewModels<CompanyListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_company_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadCompanies()
    }

    private fun initControls() {
        swipeRefreshLayout.setOnRefreshListener(this::refresh)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.apply {
            isMotionEventSplittingEnabled = false
            layoutManager = LinearLayoutManager(context)

            adapter = CompaniesAdapter().apply {
                actionListener = this@CompanyListFragment
            }

            addItemDecoration(ItemsDecoration(context, null, null, null, R.dimen.item_spacing))
        }
    }

    private fun loadCompanies() {
        viewModel.companyList().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Loading -> {
                    progressBar.isVisible = true
                    recyclerView.isVisible = false
                    noDataTextView.isVisible = false
                }
                is State.Success -> {
                    progressBar.isVisible = false

                    if (state.data.isEmpty()) {
                        recyclerView.isVisible = false
                        noDataTextView.isVisible = true
                    } else {
                        noDataTextView.isVisible = false
                        recyclerView.isVisible = true
                        (recyclerView.adapter as CompaniesAdapter).replaceCompanies(state.data)
                    }
                }
                is State.Failure -> {
                    progressBar.isVisible = false
                    noDataTextView.isVisible = !recyclerView.isVisible

                    view?.let {
                        Snackbar
                            .make(it, state.throwable.localizedMessage, Snackbar.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
    }

    private fun refresh() {
        viewModel.refresh().also { liveData ->
            liveData.observe(viewLifecycleOwner, ResultObserver(liveData) { state ->
                when (state) {
                    is State.Success -> {
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is State.Failure -> {
                        swipeRefreshLayout.isRefreshing = false

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

    override fun onItemClicked(item: Company) {
        view?.findNavController()?.navigate(
            CompanyListFragmentDirections.toCompanyDetailsFragment(item.companyName)
        )
    }
}