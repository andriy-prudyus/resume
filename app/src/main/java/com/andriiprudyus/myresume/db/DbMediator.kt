package com.andriiprudyus.myresume.db

import android.content.Context
import com.andriiprudyus.myresume.db.achievement.AchievementDao
import com.andriiprudyus.myresume.db.company.CompanyDao
import com.andriiprudyus.myresume.db.responsibility.ResponsibilityDao
import com.andriiprudyus.myresume.db.role.RoleDao

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