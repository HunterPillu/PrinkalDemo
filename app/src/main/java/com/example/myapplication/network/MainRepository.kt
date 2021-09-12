package com.example.myapplication.network

class MainRepository constructor(private val retrofitService: RetrofitService) {

    fun getRoutes(latlongs : String) = retrofitService.getRoutes(latlongs)
}