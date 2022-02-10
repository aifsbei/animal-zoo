package com.tmvlg.zooanimal.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://zoo-animal-api.herokuapp.com/"

private var retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object AnimalZooApi {
    val retrofitService: AnimalZooApiService by lazy {
        retrofit.create(AnimalZooApiService::class.java)
    }
}