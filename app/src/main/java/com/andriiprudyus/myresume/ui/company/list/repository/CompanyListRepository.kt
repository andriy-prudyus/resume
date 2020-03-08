package com.andriiprudyus.myresume.ui.company.list.repository

import com.andriiprudyus.database.CompanyWithRelations
import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.database.company.DbCompany
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.myresume.converter.toCompaniesWithRelations
import com.andriiprudyus.myresume.sharedPreferences.CompanySharedPreferences
import com.andriiprudyus.network.CompanyService
import com.andriiprudyus.network.model.CompanyDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CompanyListRepository @Inject constructor(
    private val companyService: CompanyService,
    private val dbMediator: DbMediator,
    private val sharedPreferences: CompanySharedPreferences,
    private val calendar: Calendar
) {

    companion object {
        private const val FILE_NAME = "Companies.json"
        private const val CACHE_EXPIRATION = 86400000 // ms (1 day)
    }

    fun loadCompanies(): Single<List<Company>> {
        return Single.fromCallable {
                sharedPreferences.lastLoadDataTimestamp
            }
            .flatMap {
                if (calendar.timeInMillis - it > CACHE_EXPIRATION) {
                    loadCompaniesFromServer()
                        .map { companies ->
                            companies.toCompaniesWithRelations()
                        }
                        .flatMap { companiesWithRelations ->
                            dbMediator.companyDao.delete().toSingleDefault(companiesWithRelations)
                        }
                        .flatMap { companiesWithRelations ->
                            saveCompanies(companiesWithRelations).toSingleDefault(Any())
                        }
                        .map {
                            sharedPreferences.lastLoadDataTimestamp = calendar.timeInMillis
                        }
                } else {
                    Single.just(Any())
                }
            }
            .flatMap {
                dbMediator.companyDao.selectCompanies()
            }
    }

    fun refreshCompanies(): Single<List<Company>> {
        return Single.fromCallable {
                sharedPreferences.lastLoadDataTimestamp = 0
            }
            .flatMap {
                loadCompanies()
            }
    }

    private fun loadCompaniesFromServer(): Single<List<CompanyDto>> {
        return companyService.loadCompanies()
            .flatMap { response ->
                val token = object : TypeToken<List<CompanyDto>>() {}.type
                val content = response.body()!!.files[FILE_NAME]?.content

                if (content == null) {
                    Single.error(IllegalArgumentException("File content is invalid")) //TODO: Use localized string
                } else {
                    Single.just(Gson().fromJson<List<CompanyDto>>(content, token))
                }
            }
    }

    private fun saveCompanies(companiesWithRelations: List<CompanyWithRelations>): Completable {
        return Completable.fromCallable {
            dbMediator.runInTransaction { dbMediator ->
                val companies = mutableListOf<DbCompany>()
                val roles = mutableListOf<DbRole>()
                val responsibilities = mutableListOf<DbResponsibility>()
                val achievements = mutableListOf<DbAchievement>()

                companiesWithRelations.forEach { companyWithRelations ->
                    companies.add(companyWithRelations.company)

                    companyWithRelations.rolesWithRelations.forEach { roleWithRelations ->
                        roles.add(roleWithRelations.role)
                        responsibilities.addAll(roleWithRelations.responsibilities)
                        achievements.addAll(roleWithRelations.achievements)
                    }
                }

                dbMediator.run {
                    companyDao.insert(companies)
                    roleDao.insert(roles)
                    responsibilityDao.insert(responsibilities)
                    achievementDao.insert(achievements)
                }
            }
        }
    }
}