package com.andriiprudyus.network.model

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("summary")
    val summary: String,

    @SerializedName("logo_url")
    val logoUrl: String,

    @SerializedName("roles")
    val roles: List<RoleDto>
)