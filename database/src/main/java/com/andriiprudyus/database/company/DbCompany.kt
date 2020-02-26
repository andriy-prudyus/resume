package com.andriiprudyus.database.company

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DbCompany(
    @PrimaryKey
    val companyName: String,

    val summary: String,
    val logoUrl: String
)