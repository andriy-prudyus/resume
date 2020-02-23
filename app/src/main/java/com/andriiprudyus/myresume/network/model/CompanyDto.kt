package com.andriiprudyus.myresume.network.model

import com.google.gson.annotations.SerializedName

data class CompanyDto(
    @SerializedName("company_name")
    val companyName: String,

    @SerializedName("logo_url")
    val logoUrl: String,

    @SerializedName("roles")
    val roles: List<RoleDto>,

    @SerializedName("summary")
    val summary: String
)