package com.example.myapplication.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.RouteResponse
import com.example.myapplication.network.MainRepository
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardViewModel constructor(private val repository: MainRepository): ViewModel() {

    val routeData = MutableLiveData<RouteResponse>()
    val errorMessage = MutableLiveData<String>()

    fun getRoutes(latlngs :String) {

        val response = repository.getRoutes(latlngs)
        response.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                // Log.e("onResult",response.toString())
                routeData.postValue(response.body())
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}