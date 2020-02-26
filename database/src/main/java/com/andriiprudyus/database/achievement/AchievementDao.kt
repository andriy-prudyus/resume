package com.andriiprudyus.database.achievement

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao
import io.reactivex.Single

@Dao
abstract class AchievementDao : BaseDao<DbAchievement>() {

    @Query("SELECT * FROM DbAchievement WHERE companyName = :companyName")
    abstract fun select(companyName: String): Single<List<DbAchievement>>
}