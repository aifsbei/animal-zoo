package com.tmvlg.zooanimal.data.network.models


import com.google.gson.annotations.SerializedName

data class AnimalResponse(
    @SerializedName("active_time")
    val activeTime: String, // Nocturnal
    @SerializedName("animal_type")
    val animalType: String, // Reptile
    val diet: String, // Fish, mussels, and other small animals
    @SerializedName("geo_range")
    val geoRange: String, // Eastern China
    val habitat: String, // Rivers, streams, marshes, and other bodies of water
    val id: Int, // 52
    @SerializedName("image_link")
    val imageLink: String, // https://upload.wikimedia.org/wikipedia/commons/2/24/China-Alligator.jpg
    @SerializedName("latin_name")
    val latinName: String, // Alligator sinensis
    @SerializedName("length_max")
    val lengthMax: String, // 6.5
    @SerializedName("length_min")
    val lengthMin: String, // 5.5
    val lifespan: String, // 31
    val name: String, // Chinese Alligator
    @SerializedName("weight_max")
    val weightMax: String, // 85
    @SerializedName("weight_min")
    val weightMin: String // 50
)