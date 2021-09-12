package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Geometry(
    @Expose
    @SerializedName("coordinates")
    val coordinates: List<List<Double>>,
    @Expose
    @SerializedName("type")
    val type: String
)