package com.andriiprudyus.myresume.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andriiprudyus.myresume.db.achievement.AchievementDao
import com.andriiprudyus.myresume.db.achievement.DbAchievement
import com.andriiprudyus.myresume.db.company.Company
import com.andriiprudyus.myresume.db.company.CompanyDao
import com.andriiprudyus.myresume.db.company.DbCompany
import com.andriiprudyus.myresume.db.responsibility.DbResponsibility
import com.andriiprudyus.myresume.db.responsibility.ResponsibilityDao
import com.andriiprudyus.myresume.db.role.DbRole
import com.andriiprudyus.myresume.db.role.RoleDao

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