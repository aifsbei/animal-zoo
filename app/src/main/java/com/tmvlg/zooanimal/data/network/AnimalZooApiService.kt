package com.tmvlg.zooanimal.data.network

import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import retrofit2.Call
import retrofit2.http.GET

interface AnimalZooApiService {

    @GET("animals/rand")
    fun getAnimal(): Call<AnimalResponse>

    @GET("animals/rand/10")
    fun get10Animals(): Call<List<AnimalResponse>>

}