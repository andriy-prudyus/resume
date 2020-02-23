package com.andriiprudyus.myresume.db.company

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.myresume.db.BaseDao
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class CompanyDao : BaseDao<DbCompany>() {

    @Query("SELECT * FROM Company")
    abstract fun selectCompanies(): Single<List<Company>>

    @Query("SELECT summary FROM DbCompany WHERE companyName = :companyName")
    abstract fun selectSummary(companyName: String): Single<String>

    @Query("DELETE FROM DbCompany")
    abstract fun delete(): Completable
}