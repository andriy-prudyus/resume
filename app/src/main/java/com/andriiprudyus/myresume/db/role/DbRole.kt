package com.andriiprudyus.myresume.db.role

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andriiprudyus.myresume.db.company.DbCompany

@Entity(
    indices = [
        Index(value = ["companyName", "roleName"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = DbCompany::class,
            parentColumns = ["companyName"],
            childColumns = ["companyName"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbRole(
    val roleName: String,
    val companyName: String,
    val startedAt: Long,
    val endedAt: Long,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)