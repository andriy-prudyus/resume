package com.andriiprudyus.database.responsibility

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao
import io.reactivex.Single

@Dao
abstract class ResponsibilityDao : BaseDao<DbResponsibility>() {

    @Query("SELECT * FROM DbResponsibility WHERE companyName = :companyName")
    abstract fun select(companyName: String): Single<List<DbResponsibility>>
}