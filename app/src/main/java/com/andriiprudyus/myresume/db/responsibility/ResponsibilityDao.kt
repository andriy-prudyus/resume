package com.andriiprudyus.myresume.db.responsibility

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.myresume.db.BaseDao
import io.reactivex.Single

@Dao
abstract class ResponsibilityDao : BaseDao<DbResponsibility>() {

    @Query(
        """
        SELECT 
            roleName,
            responsibilityName 
        FROM DbResponsibility 
        WHERE companyName = :companyName
    """
    )
    abstract fun select(companyName: String): Single<List<Responsibility>>
}