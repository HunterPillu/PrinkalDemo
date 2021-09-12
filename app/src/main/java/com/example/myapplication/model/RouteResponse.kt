package com.example.myapplication.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RouteResponse(
    @Expose
    @SerializedName("routes")
    val routes: List<Routes>,
    @Expose
    @SerializedName("code")
    val code: String,
    @Expose
    @SerializedName("Server")
    val Server: String,
    @Expose
    @SerializedName("version")
    val version: String,

    @Expose
    @SerializedName("waypoints")
    val waypoints: List<Waypoints>,
)