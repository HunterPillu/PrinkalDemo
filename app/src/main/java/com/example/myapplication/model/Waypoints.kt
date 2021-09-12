package com.example.myapplication.model
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class Waypoints(
    @Expose
    @SerializedName("distance")
    val distance: Double,
    @Expose
    @SerializedName("hint")
    val hint: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("location")
    val location: List<Double>
)