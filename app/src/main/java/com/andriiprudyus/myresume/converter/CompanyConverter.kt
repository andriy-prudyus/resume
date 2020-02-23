package com.andriiprudyus.myresume.converter

import com.andriiprudyus.myresume.db.CompanyWithRelations
import com.andriiprudyus.myresume.db.RoleWithRelations
import com.andriiprudyus.myresume.db.achievement.DbAchievement
import com.andriiprudyus.myresume.db.company.DbCompany
import com.andriiprudyus.myresume.db.responsibility.DbResponsibility
import com.andriiprudyus.myresume.db.role.DbRole
import com.andriiprudyus.myresume.network.model.CompanyDto
import com.andriiprudyus.myresume.network.model.RoleDto

fun List<CompanyDto>.toCompaniesWithRelations(): List<CompanyWithRelations> {
    return map {
        it.toCompanyWithRelations()
    }
}

private fun CompanyDto.toCompanyWithRelations(): CompanyWithRelations {
    return CompanyWithRelations(
        DbCompany(companyName, summary, logoUrl),
        roles.toRolesWithRelations(companyName)
    )
}

private fun List<RoleDto>.toRolesWithRelations(companyName: String): List<RoleWithRelations> {
    return map {
        it.toRoleWithRelations(companyName)
    }
}

private fun RoleDto.toRoleWithRelations(companyName: String): RoleWithRelations {
    return RoleWithRelations(
        DbRole(roleName, companyName, startedAt, endedAt),
        responsibilities.map { responsibility ->
            DbResponsibility(companyName, roleName, responsibility)
        },
        achievements.map { achievement ->
            DbAchievement(companyName, roleName, achievement)
        }
    )
}