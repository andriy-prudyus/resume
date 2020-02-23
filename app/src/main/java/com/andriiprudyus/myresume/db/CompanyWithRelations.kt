package com.andriiprudyus.myresume.db

import com.andriiprudyus.myresume.db.company.DbCompany

data class CompanyWithRelations(
    val company: DbCompany,
    val rolesWithRelations: List<RoleWithRelations>
)