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
class ApiUnitTest {
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
    @Test
    fun load10Animals() {

        val animalsList = mutableListOf<Animal>()

        val animalsResponse: Response<List<AnimalResponse>> = AnimalZooApi
            .retrofitService
            .get10Animals()
            .execute() ?: throw RuntimeException("loading failed!")

        val animalsBody = animalsResponse.body()
            ?: throw RuntimeException("loading failed!")

        animalsBody.forEach { item ->

            val animal = Animal(
                name = item.name,
                latinName = item.latinName,
                activeTime = item.activeTime,
                averageLength = (item.lengthMin.toFloat() + item.lengthMax.toFloat()) / 2,
                averageWeight = (item.weightMin.toFloat() + item.weightMax.toFloat()) / 2,
                lifespan = item.lifespan.toInt(),
                habitat = item.habitat,
                diet = item.diet,
                geo = item.geoRange,
                imageUrl = item.imageLink,
            )

            animalsList.add(animal)

        }

        println(animalsList)

    }
}