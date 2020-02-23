package com.andriiprudyus.myresume.db.achievement

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.andriiprudyus.myresume.db.role.DbRole

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