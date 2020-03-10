package com.andriiprudyus.myresume.ui.company.details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andriiprudyus.myresume.base.viewModel.State
import com.andriiprudyus.myresume.ui.company.details.adapter.RolesAdapter
import com.andriiprudyus.myresume.ui.company.details.repository.CompanyDetailsRepository
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class CompanyDetailsViewModel @Inject constructor(
    private val repository: CompanyDetailsRepository
) : ViewModel() {

    var companyName = ""

    val items: LiveData<State<List<RolesAdapter.Item>>> by lazy {
        MutableLiveData<State<List<RolesAdapter.Item>>>().also { result ->
            viewModelScope.launch(CoroutineExceptionHandler { _, e ->
                Timber.e(e)
                result.value = State.Failure(e)
            }) {
                result.value = State.Loading()
                result.value = State.Success(loadItems())
            }
        }
    }

    private suspend fun loadItems(): List<RolesAdapter.Item> {
        return withContext(Dispatchers.IO) {
            val deferredSummary = async { repository.loadSummary(companyName) }
            val deferredRoles = async { repository.loadRoles(companyName) }
            val deferredResponsibilities = async { repository.loadResponsibilities(companyName) }
            val deferredAchievements = async { repository.loadAchievements(companyName) }

            val summary = deferredSummary.await()
            val roles = deferredRoles.await()
            val responsibilities = deferredResponsibilities.await()
            val achievements = deferredAchievements.await()

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
    }
}