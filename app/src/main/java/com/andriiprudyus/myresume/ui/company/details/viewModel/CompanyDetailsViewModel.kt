package com.andriiprudyus.myresume.ui.company.details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andriiprudyus.myresume.base.viewModel.BaseViewModel
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.db.achievement.Achievement
import com.andriiprudyus.myresume.db.responsibility.Responsibility
import com.andriiprudyus.myresume.db.role.Role
import com.andriiprudyus.myresume.di.Injector
import com.andriiprudyus.myresume.log.AppLog
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.repository.CompanyDetailsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

class CompanyDetailsViewModel(
    private val repository: CompanyDetailsRepository = Injector.companyDetailsRepository
) : BaseViewModel() {

    constructor(companyName: String) : this() {
        this.companyName = companyName
    }

    var companyName = ""

    private val items = MutableLiveData<State<List<RolesAdapter.Item>>>()

    fun getItems(): LiveData<State<List<RolesAdapter.Item>>> {
        Single.zip(
            repository.loadSummary(companyName),
            repository.loadRoles(companyName),
            repository.loadResponsibilities(companyName),
            repository.loadAchievements(companyName),
            Function4<String, List<Role>, List<Responsibility>, List<Achievement>, List<RolesAdapter.Item>> { summary, roles, responsibilities, achievements ->
                val items = mutableListOf<RolesAdapter.Item>()
                items.add(RolesAdapter.Item.Summary(summary))

                roles.forEach { role ->
                    items.add(RolesAdapter.Item.Role(role.roleName, role.startedAt, role.endedAt))

                    items.addAll(
                        responsibilities
                            .filter { responsibility -> responsibility.roleName == role.roleName }
                            .map {
                                RolesAdapter.Item.Responsibility(it.responsibilityName)
                            }
                    )

                    items.addAll(
                        achievements
                            .filter { achievement -> achievement.roleName == role.roleName }
                            .map {
                                RolesAdapter.Item.Achievement(it.achievementName)
                            }
                    )
                }

                items
            }
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                items.value = State.Loading()
            }
            .subscribe({
                items.value = State.Success(it)
            }, {
                AppLog.e(it)
                items.value = State.Failure(it)
            })
            .also {
                compositeDisposable.add(it)
            }

        return items
    }
}