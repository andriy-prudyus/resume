package com.andriiprudyus.database

import android.content.Context
import com.andriiprudyus.database.achievement.AchievementDao
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.database.responsibility.ResponsibilityDao
import com.andriiprudyus.database.role.RoleDao

class DbMediator(context: Context) {

    private val db: AppDatabase = AppDatabase.buildDatabase(context)

    val companyDao: CompanyDao = db.companyDao()
    val roleDao: RoleDao = db.roleDao()
    val responsibilityDao: ResponsibilityDao = db.responsibilityDao()
    val achievementDao: AchievementDao = db.achievementDao()

    fun runInTransaction(body: (dbMediator: DbMediator) -> Unit) {
        db.runInTransaction {
            body.invoke(this)
        }
    }
}