package com.andriiprudyus.myresume.ui.company.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.myresume.base.viewModel.BaseViewModel
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.list.repository.CompanyListRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CompanyListViewModel @Inject constructor(
    private val repository: CompanyListRepository
) : BaseViewModel() {

    private val companyList = MutableLiveData<State<List<Company>>>()

    fun companyList(): LiveData<State<List<Company>>> {
        repository.loadCompanies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                companyList.value = State.Loading()
            }
            .subscribe({
                companyList.value = State.Success(it)
            }, {
                Timber.e(it)
                companyList.value = State.Failure(it)
            })
            .also {
                compositeDisposable.add(it)
            }

        return companyList
    }

    fun refresh(): LiveData<State<Any>> {
        return MutableLiveData<State<Any>>().also { result ->
            repository.refreshCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    result.value = State.Loading()
                }
                .subscribe({
                    result.value = State.Success(Any())
                    companyList.value = State.Success(it)
                }, {
                    Timber.e(it)
                    result.value = State.Failure(it)
                })
                .also {
                    compositeDisposable.add(it)
                }
        }
    }
}