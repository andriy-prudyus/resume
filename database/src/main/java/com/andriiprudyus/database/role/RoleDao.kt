package com.andriiprudyus.database.role

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao
import io.reactivex.Single

@Dao
abstract class RoleDao : BaseDao<DbRole>() {

    @Query(
        """
        SELECT 
            roleName,
            startedAt,
            endedAt
        FROM DbRole
        WHERE companyName = :companyName
        ORDER BY endedAt DESC
    """
    )
    abstract fun select(companyName: String): Single<List<Role>>
}