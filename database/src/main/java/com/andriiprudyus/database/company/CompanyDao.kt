package com.andriiprudyus.database.company

import androidx.room.Dao
import androidx.room.Query
import com.andriiprudyus.database.BaseDao

@Dao
abstract class CompanyDao : BaseDao<DbCompany>() {

    @Query("SELECT * FROM Company")
    abstract suspend fun selectCompanies(): List<Company>

    @Query("SELECT summary FROM DbCompany WHERE companyName = :companyName")
    abstract suspend fun selectSummary(companyName: String): String

    @Query("DELETE FROM DbCompany")
    abstract suspend fun delete(): Int
}