package com.example.dosomas.network

import com.example.dosomas.model.MyDataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("endpoint/{id}")
    fun getData(@Path("id") id: Int): Call<MyDataResponse>
}
