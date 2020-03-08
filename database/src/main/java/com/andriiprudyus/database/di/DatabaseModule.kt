package com.andriiprudyus.database.di

import android.content.Context
import androidx.room.Room
import com.andriiprudyus.database.AppDatabase
import com.andriiprudyus.database.achievement.AchievementDao
import com.andriiprudyus.database.company.CompanyDao
import com.andriiprudyus.database.responsibility.ResponsibilityDao
import com.andriiprudyus.database.role.RoleDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(subcomponents = [DatabaseComponent::class])
class DatabaseModule {

    companion object {
        private const val DB_NAME = "database"
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideCompanyDao(db: AppDatabase): CompanyDao = db.companyDao()

    @Singleton
    @Provides
    fun provideRoleDao(db: AppDatabase): RoleDao = db.roleDao()

    @Singleton
    @Provides
    fun provideResponsibilityDao(db: AppDatabase): ResponsibilityDao = db.responsibilityDao()

    @Singleton
    @Provides
    fun provideAchievementDao(db: AppDatabase): AchievementDao = db.achievementDao()
}