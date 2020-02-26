package com.andriiprudyus.database

import com.andriiprudyus.database.company.DbCompany

data class CompanyWithRelations(
    val company: DbCompany,
    val rolesWithRelations: List<RoleWithRelations>
)