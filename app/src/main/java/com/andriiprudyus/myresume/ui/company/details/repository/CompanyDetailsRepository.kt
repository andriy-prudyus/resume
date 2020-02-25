package com.andriiprudyus.myresume.ui.company.details.repository

import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.Achievement
import com.andriiprudyus.database.responsibility.Responsibility
import com.andriiprudyus.database.role.Role
import io.reactivex.Single

class CompanyDetailsRepository(private val dbMediator: DbMediator) {

    fun loadSummary(companyName: String): Single<String> {
        return dbMediator.companyDao.selectSummary(companyName)
    }

    fun loadRoles(companyName: String): Single<List<Role>> {
        return dbMediator.roleDao.select(companyName)
    }

    fun loadResponsibilities(companyName: String): Single<List<Responsibility>> {
        return dbMediator.responsibilityDao.select(companyName)
    }

    fun loadAchievements(companyName: String): Single<List<Achievement>> {
        return dbMediator.achievementDao.select(companyName)
    }
}