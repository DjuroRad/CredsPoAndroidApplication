package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName

data class RegisterVerificationBody(
    val email: String,
    val password: String,
    @SerializedName("verification_code")
    val verificationCode: String
)
