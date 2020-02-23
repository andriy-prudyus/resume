package com.andriiprudyus.myresume.db

import com.andriiprudyus.myresume.db.achievement.DbAchievement
import com.andriiprudyus.myresume.db.responsibility.DbResponsibility
import com.andriiprudyus.myresume.db.role.DbRole

data class RoleWithRelations(
    val role: DbRole,
    val responsibilities: List<DbResponsibility>,
    val achievements: List<DbAchievement>
)