package com.andriiprudyus.myresume.base.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.andriiprudyus.myresume.ui.company.details.view.CompanyDetailsFragment
import com.andriiprudyus.myresume.ui.company.list.view.CompanyListFragment
import javax.inject.Inject

class AppFragmentFactory @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            CompanyListFragment::class.java -> CompanyListFragment(viewModelFactory)
            CompanyDetailsFragment::class.java -> CompanyDetailsFragment(viewModelFactory)
            else -> super.instantiate(classLoader, className)
        }
    }
}