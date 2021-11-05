package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserProgressResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<UserProgress>
):Serializable
