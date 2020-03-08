package com.andriiprudyus.myresume.ui.company.details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.myresume.base.viewModel.BaseViewModel
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.repository.CompanyDetailsRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CompanyDetailsViewModel @Inject constructor(
    private val repository: CompanyDetailsRepository
) : BaseViewModel() {

    var companyName = ""

    private val items = MutableLiveData<State<List<RolesAdapter.Item>>>()

    fun getItems(): LiveData<State<List<RolesAdapter.Item>>> {
        Single.zip(
                repository.loadSummary(companyName),
                repository.loadRoles(companyName),
                repository.loadResponsibilities(companyName),
                repository.loadAchievements(companyName),
                Function4<String, List<DbRole>, List<DbResponsibility>, List<DbAchievement>, List<RolesAdapter.Item>> { summary, roles, responsibilities, achievements ->
                    val items = mutableListOf<RolesAdapter.Item>()
                    items.add(RolesAdapter.Item.Summary(summary))

                    roles.forEach { role ->
                        items.add(RolesAdapter.Item.Role(role))

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
                Timber.e(it)
                items.value = State.Failure(it)
            })
            .also {
                compositeDisposable.add(it)
            }

        return items
    }
}