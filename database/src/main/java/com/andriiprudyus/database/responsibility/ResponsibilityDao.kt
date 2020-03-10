package com.andriiprudyus.database.responsibility

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao

@Dao
abstract class ResponsibilityDao : BaseDao<DbResponsibility>() {

    @Query("SELECT * FROM DbResponsibility WHERE companyName = :companyName")
    abstract suspend fun select(companyName: String): List<DbResponsibility>
}