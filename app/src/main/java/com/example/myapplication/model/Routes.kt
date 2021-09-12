package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Routes(
    @Expose
    @SerializedName("duration")
    val duration: Double,
    @Expose
    @SerializedName("distance")
    val distance: Double,
    @Expose
    @SerializedName("legs")
    val legs: List<Legs>,
    @Expose
    @SerializedName("weight_name")
    val weight_name: String,
    @Expose
    @SerializedName("weight")
    val weight: Double,

    @Expose
    @SerializedName("geometry")
    val geometry: Geometry
)