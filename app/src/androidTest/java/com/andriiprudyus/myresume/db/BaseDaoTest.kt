package com.andriiprudyus.myresume.db

import android.content.Context
import androidx.annotation.CallSuper
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.andriiprudyus.myresume.db.achievement.DbAchievement
import com.andriiprudyus.myresume.db.company.DbCompany
import com.andriiprudyus.myresume.db.responsibility.DbResponsibility
import com.andriiprudyus.myresume.db.role.DbRole
import com.andriiprudyus.myresume.db.utils.fromJson
import com.andriiprudyus.myresume.test.R
import com.google.gson.reflect.TypeToken
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
abstract class BaseDaoTest {

    protected lateinit var db: AppDatabase

    private lateinit var context: Context

    @Before
    @CallSuper
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().context

        db = Room
            .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        populateDb()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private fun populateDb() {
        insertCompanies()
        insertRoles()
        insertResponsibilities()
        insertAchievements()
    }

    private fun insertCompanies() {
        db.companyDao().insert(
            fromJson<List<DbCompany>>(
                context,
                R.raw.db_test_companies,
                object : TypeToken<List<DbCompany>>() {}.type
            )
        )
    }

    private fun insertRoles() {
        db.roleDao().insert(
            fromJson<List<DbRole>>(
                context,
                R.raw.db_test_roles,
                object : TypeToken<List<DbRole>>() {}.type
            )
        )
    }

    private fun insertResponsibilities() {
        db.responsibilityDao().insert(
            fromJson<List<DbResponsibility>>(
                context,
                R.raw.db_test_responsibilities,
                object : TypeToken<List<DbResponsibility>>() {}.type
            )
        )
    }

    private fun insertAchievements() {
        db.achievementDao().insert(
            fromJson<List<DbAchievement>>(
                context,
                R.raw.db_test_achievements,
                object : TypeToken<List<DbAchievement>>() {}.type
            )
        )
    }
}