package com.andriiprudyus.database.achievement

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andriiprudyus.database.role.DbRole

@Entity(
    indices = [
        Index(value = ["companyName", "roleName", "achievementName"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = DbRole::class,
            parentColumns = ["companyName", "roleName"],
            childColumns = ["companyName", "roleName"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbAchievement(
    val companyName: String,
    val roleName: String,
    val achievementName: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
)