package com.andriiprudyus.myresume.ui.company.details.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CompanyDetailsViewModelFactory(var companyName: String = "") : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java).newInstance(companyName)
    }
}