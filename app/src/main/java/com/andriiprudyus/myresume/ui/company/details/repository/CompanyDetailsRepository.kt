package com.andriiprudyus.myresume.ui.company.details.repository

import com.andriiprudyus.database.DbMediator
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import io.reactivex.Single
import javax.inject.Inject

class CompanyDetailsRepository @Inject constructor(private val dbMediator: DbMediator) {

    fun loadSummary(companyName: String): Single<String> {
        return dbMediator.companyDao.selectSummary(companyName)
    }

    fun loadRoles(companyName: String): Single<List<DbRole>> {
        return dbMediator.roleDao.select(companyName)
    }

    fun loadResponsibilities(companyName: String): Single<List<DbResponsibility>> {
        return dbMediator.responsibilityDao.select(companyName)
    }

    fun loadAchievements(companyName: String): Single<List<DbAchievement>> {
        return dbMediator.achievementDao.select(companyName)
    }
}