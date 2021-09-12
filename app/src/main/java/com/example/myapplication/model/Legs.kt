package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Legs(
    @Expose
    @SerializedName("summary")
    val summary: String,
    @Expose
    @SerializedName("duration")
    val duration: Double,
    @Expose
    @SerializedName("distance")
    val distance: Double,
    @Expose
    @SerializedName("weight")
    val weight: Double,
    @Expose
    @SerializedName("steps")
    val steps: List<String>
)