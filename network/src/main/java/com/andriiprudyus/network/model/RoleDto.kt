package com.andriiprudyus.network.model

import com.google.gson.annotations.SerializedName

data class RoleDto(
    @SerializedName("role_name")
    val roleName: String,

    @SerializedName("started_at")
    val startedAt: Long,

    @SerializedName("ended_at")
    val endedAt: Long,

    val responsibilities: List<String>,
    val achievements: List<String>
)