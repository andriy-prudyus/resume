package com.andriiprudyus.database.role

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao
import io.reactivex.Single

@Dao
abstract class RoleDao : BaseDao<DbRole>() {

    @Query("SELECT * FROM DbRole WHERE companyName = :companyName ORDER BY endedAt DESC")
    abstract fun select(companyName: String): Single<List<DbRole>>
}