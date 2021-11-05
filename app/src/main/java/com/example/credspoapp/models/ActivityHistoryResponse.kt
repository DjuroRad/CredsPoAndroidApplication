package com.example.credspoapp.models

import com.example.credspoapp.models.User
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ActivityHistoryResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val dataActivity: DataActivity
):Serializable