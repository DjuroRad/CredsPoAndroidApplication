package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("access_token")
    val accessToken: String,
    val user: User
)
