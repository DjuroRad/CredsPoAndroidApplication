package com.example.credspoapp.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataActivity(
    @SerializedName("badges")
    val badges: List<Badge>,
    @SerializedName("activities")
    val activities: MutableList<ActivityCredsPo>,
):Serializable