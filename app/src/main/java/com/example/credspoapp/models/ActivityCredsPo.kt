package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityCredsPo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("date")
    val date: String
):Serializable
