package com.andriiprudyus.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andriiprudyus.database.achievement.AchievementDao
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.Company
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.database.company.DbCompany
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.responsibility.ResponsibilityDao
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.database.role.RoleDao

@Database(
    entities = [
        DbCompany::class,
        DbRole::class,
        DbResponsibility::class,
        DbAchievement::class
    ],
    views = [
        Company::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private const val DB_NAME = "database"

        fun buildDatabase(context: Context): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
        }
    }

    abstract fun companyDao(): CompanyDao
    abstract fun roleDao(): RoleDao
    abstract fun responsibilityDao(): ResponsibilityDao
    abstract fun achievementDao(): AchievementDao
}