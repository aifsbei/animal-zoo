package com.tmvlg.zooanimal.data.network

import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import retrofit2.Call
import retrofit2.http.GET

interface AnimalZooApiService {

    @GET("animals/rand")
    fun getAnimal(): Call<AnimalResponse>
}