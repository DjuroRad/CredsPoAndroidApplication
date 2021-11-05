package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Badge(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
): Serializable
