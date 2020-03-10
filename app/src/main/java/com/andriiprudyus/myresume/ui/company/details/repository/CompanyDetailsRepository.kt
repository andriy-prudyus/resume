package com.andriiprudyus.myresume.ui.company.details.repository

import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import javax.inject.Inject

class CompanyDetailsRepository @Inject constructor(private val dbMediator: DbMediator) {

    suspend fun loadSummary(companyName: String): String {
        return dbMediator.companyDao.selectSummary(companyName)
    }

    suspend fun loadRoles(companyName: String): List<DbRole> {
        return dbMediator.roleDao.select(companyName)
    }

    suspend fun loadResponsibilities(companyName: String): List<DbResponsibility> {
        return dbMediator.responsibilityDao.select(companyName)
    }

    suspend fun loadAchievements(companyName: String): List<DbAchievement> {
        return dbMediator.achievementDao.select(companyName)
    }
}