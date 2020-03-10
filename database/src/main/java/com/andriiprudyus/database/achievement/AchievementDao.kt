package com.andriiprudyus.database.achievement

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao

@Dao
abstract class AchievementDao : BaseDao<DbAchievement>() {

    @Query("SELECT * FROM DbAchievement WHERE companyName = :companyName")
    abstract suspend fun select(companyName: String): List<DbAchievement>
}