package com.tmvlg.zooanimal

import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.network.AnimalZooApi
import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import com.tmvlg.zooanimal.data.repository.AnimalRepository
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Response
import java.lang.RuntimeException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun loadAnimal() {
        val animalsResponse: Response<AnimalResponse> = AnimalZooApi
            .retrofitService
            .getAnimal()
            .execute() ?: throw RuntimeException("loading failed!")

        val animalsBody: AnimalResponse = animalsResponse.body()
            ?: throw RuntimeException("loading failed!")

        val animal = Animal(
            name = animalsBody.name,
            latinName = animalsBody.latinName,
            activeTime = animalsBody.activeTime,
            averageLength = (animalsBody.lengthMin.toFloat() + animalsBody.lengthMax.toFloat()) / 2,
            averageWeight = (animalsBody.weightMin.toFloat() + animalsBody.weightMax.toFloat()) / 2,
            lifespan = animalsBody.lifespan.toInt(),
            habitat = animalsBody.habitat,
            diet = animalsBody.diet,
            geo = animalsBody.geoRange,
            imageUrl = animalsBody.imageLink,
        )

        println(animal)
    }
}