package com.andriiprudyus.myresume.ui.company.list.repository

import com.andriiprudyus.database.CompanyWithRelations
import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.database.company.DbCompany
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.myresume.converter.toCompaniesWithRelations
import com.andriiprudyus.myresume.error.AppException
import com.andriiprudyus.myresume.error.ErrorCode
import com.andriiprudyus.myresume.sharedPreferences.CompanySharedPreferences
import com.andriiprudyus.network.CompanyService
import com.andriiprudyus.network.model.CompanyDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun loadCompanies(): List<Company> {
        return withContext(Dispatchers.IO) {
            if (calendar.timeInMillis - sharedPreferences.lastLoadDataTimestamp > CACHE_EXPIRATION) {
                val companies = loadCompaniesFromServer()
                dbMediator.companyDao.delete()
                saveCompanies(companies.toCompaniesWithRelations())
                sharedPreferences.lastLoadDataTimestamp = calendar.timeInMillis
            }

            dbMediator.companyDao.selectCompanies()
        }
    }

    suspend fun refreshCompanies(): List<Company> {
        sharedPreferences.lastLoadDataTimestamp = 0
        return loadCompanies()
    }

    @Throws(AppException::class)
    private suspend fun loadCompaniesFromServer(): List<CompanyDto> {
        val response = companyService.loadCompanies()
        val token = object : TypeToken<List<CompanyDto>>() {}.type
        val content = response.body()!!.files[FILE_NAME]?.content

        return Gson().fromJson(content, token) ?: throw AppException(ErrorCode.INVALID_FILE_CONTENT)
    }

    private suspend fun saveCompanies(companiesWithRelations: List<CompanyWithRelations>) {
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