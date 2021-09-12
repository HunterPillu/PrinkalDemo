package com.example.myapplication.network

import com.example.myapplication.model.RouteResponse
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface RetrofitService {
    // https://apis.mapmyindia.com/advancedmaps/v1/bbc5dd2d6e760d0d12ca59c4e9a302ea/route_adv/driving/77.131123,28.552413;17ZUL7?
    @GET("bbc5dd2d6e760d0d12ca59c4e9a302ea/route_eta/driving/{latlngs}?geometries=geojson")
    fun getRoutes(@Path("latlngs") latlngs:String): Call<RouteResponse>


    companion object {

        var retrofitService: RetrofitService? = null

        fun getInstance() : RetrofitService {

            if (retrofitService == null) {
                val logging = HttpLoggingInterceptor()
// set your desired log level
// set your desired log level
                logging.level = HttpLoggingInterceptor.Level.BODY

                val httpClient = OkHttpClient.Builder()
// add your other interceptors …

// add logging as last interceptor
// add your other interceptors …

// add logging as last interceptor
                httpClient.addInterceptor(logging) // <-- this is the important line!

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apis.mapmyindia.com/advancedmaps/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}