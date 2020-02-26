package com.andriiprudyus.myresume.converter

import com.andriiprudyus.database.CompanyWithRelations
import com.andriiprudyus.database.RoleWithRelations
import com.andriiprudyus.database.achievement.DbAchievement
import com.andriiprudyus.database.company.DbCompany
import com.andriiprudyus.database.responsibility.DbResponsibility
import com.andriiprudyus.database.role.DbRole
import com.andriiprudyus.network.model.CompanyDto
import com.andriiprudyus.network.model.RoleDto

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