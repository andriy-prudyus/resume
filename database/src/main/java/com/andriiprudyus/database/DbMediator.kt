package com.andriiprudyus.database

import com.andriiprudyus.database.achievement.AchievementDao
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.database.responsibility.ResponsibilityDao
import com.andriiprudyus.database.role.RoleDao
import javax.inject.Inject

class DbMediator @Inject constructor(
    private val db: AppDatabase,
    val companyDao: CompanyDao,
    val roleDao: RoleDao,
    val responsibilityDao: ResponsibilityDao,
    val achievementDao: AchievementDao
) {

    fun runInTransaction(body: (dbMediator: DbMediator) -> Unit) {
        db.runInTransaction {
            body.invoke(this)
        }
    }
}