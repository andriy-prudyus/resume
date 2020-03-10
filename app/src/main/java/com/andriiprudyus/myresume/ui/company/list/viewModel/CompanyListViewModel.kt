package com.andriiprudyus.myresume.ui.company.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.list.repository.CompanyListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CompanyListViewModel @Inject constructor(
    private val repository: CompanyListRepository
) : ViewModel() {

    private val companyList = MutableLiveData<State<List<Company>>>()

    fun companyList(): LiveData<State<List<Company>>> {
        companyList.value = State.Loading()

        viewModelScope.launch(CoroutineExceptionHandler { _, e ->
            Timber.e(e)
            companyList.value = State.Failure(e)
        }) {
            companyList.value = State.Success(repository.loadCompanies())
        }

        return companyList
    }

    fun refresh(): LiveData<State<Any>> {
        return MutableLiveData<State<Any>>().also { result ->
            viewModelScope.launch(CoroutineExceptionHandler { _, e ->
                Timber.e(e)
                result.value = State.Failure(e)
            }) {
                result.value = State.Loading()
                val companies = repository.refreshCompanies()
                companyList.value = State.Success(companies)
                result.value = State.Success(Any())
            }
        }
    }
}