package com.andriiprudyus.myresume.ui.company.details.repository

import com.andriiprudyus.myresume.db.DbMediator
import com.andriiprudyus.myresume.db.achievement.Achievement
import com.andriiprudyus.myresume.db.responsibility.Responsibility
import com.andriiprudyus.myresume.db.role.Role
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