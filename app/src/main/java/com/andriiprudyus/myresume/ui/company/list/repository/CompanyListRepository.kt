package com.andriiprudyus.myresume.ui.company.list.repository

import com.andriiprudyus.myresume.converter.toCompaniesWithRelations
import com.andriiprudyus.myresume.db.CompanyWithRelations
import com.andriiprudyus.myresume.db.DbMediator
import com.andriiprudyus.myresume.db.achievement.DbAchievement
import com.andriiprudyus.myresume.db.company.Company
import com.andriiprudyus.myresume.db.company.DbCompany
import com.andriiprudyus.myresume.db.responsibility.DbResponsibility
import com.andriiprudyus.myresume.db.role.DbRole
import com.andriiprudyus.myresume.network.RestClientMediator
import com.andriiprudyus.myresume.network.model.CompanyDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Single

class CompanyListRepository(
    private val restClientMediator: RestClientMediator,
    private val dbMediator: DbMediator
) {

    companion object {
        private const val FILE_NAME = "Companies.json"
    }

    fun loadCompanies(): Single<List<Company>> {
        return dbMediator.companyDao.selectCompanies()
            .flatMap {
                if (it.isEmpty()) {
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
                        .flatMap {
                            dbMediator.companyDao.selectCompanies()
                        }
                } else {
                    Single.just(it)
                }
            }
    }

    private fun loadCompaniesFromServer(): Single<List<CompanyDto>> {
        return restClientMediator.companyApi.loadCompanies()
            .flatMap { response ->
                val token = object : TypeToken<List<CompanyDto>>() {}.type
                val content = response.body()!!.files[FILE_NAME]?.content

                if (content == null) {
                    Single.error(Exception("File content is invalid")) //TODO: Use localized string
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