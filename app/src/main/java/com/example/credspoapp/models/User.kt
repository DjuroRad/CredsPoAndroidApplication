package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName

class User (
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    @SerializedName("country_id")
    val countryId: Int,
    @SerializedName("verification_code")
    val verificationCode: Int?
    )