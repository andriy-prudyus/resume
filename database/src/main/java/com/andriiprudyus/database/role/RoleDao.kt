package com.andriiprudyus.database.role

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao

@Dao
abstract class RoleDao : BaseDao<DbRole>() {

    @Query("SELECT * FROM DbRole WHERE companyName = :companyName ORDER BY endedAt DESC")
    abstract suspend fun select(companyName: String): List<DbRole>
}