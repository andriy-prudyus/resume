package com.andriiprudyus.database.company

import androidx.room.DatabaseView

@DatabaseView(
    """
    SELECT 
        c.companyName, 
        c.logoUrl,
        c.summary,
        (SELECT MIN(startedAt) FROM DbRole WHERE companyName = c.companyName) AS startedAt,
        (SELECT MAX(endedAt) FROM DbRole WHERE companyName = c.companyName) AS endedAt,
        (SELECT roleName FROM DbRole WHERE companyName = c.companyName ORDER BY endedAt DESC LIMIT 1) AS roleName
    FROM DbCompany c
"""
)
data class Company(
    val companyName: String,
    val logoUrl: String,
    val summary: String,
    val startedAt: Long?,
    val endedAt: Long?,
    val roleName: String?
)