package com.andriiprudyus.database

import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole

data class RoleWithRelations(
    val role: DbRole,
    val responsibilities: List<DbResponsibility>,
    val achievements: List<DbAchievement>
)