package com.tmvlg.zooanimal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.network.AnimalZooApi
import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import retrofit2.Response
import java.io.IOException
import java.lang.RuntimeException

object AnimalRepository {

    private val animalLD = MutableLiveData<List<Animal>>()

    private val animalList = mutableListOf<Animal>()

    private val otherAnimalList = mutableListOf<Animal>()

    private var autoIncrementId = 0

    private var isLoading = false

    private val MAX_ATTEMPTS_COUNT = 2

    fun addAnimal() {
        Log.d("1", "addAnimal: $otherAnimalList")
        if (otherAnimalList.isNotEmpty()) {
            while (true) {
                val animal = otherAnimalList.random()
                if (animalList.contains(animal)) {
                    continue
                }
                animalList.add((0..animalList.size).random(), animal)
                updateList()
                break
            }
        }
    }

    fun addAnimalToOtherList(animal: Animal) {
        if (animal.id == Animal.UNDEFINED_ID)
            animal.id = autoIncrementId++
        Log.d("1", "addAnimalToOtherList: $animal")
        otherAnimalList.add(animal)
    }

    fun deleteAnimal(animal: Animal) {
        animalList.remove(animal)
        updateList()
    }

    fun getAnimalList(): LiveData<List<Animal>> {
        return animalLD
    }

    fun getAnimal(animalId: Int): Animal {
        return animalList.find { it.id == animalId }
            ?: throw RuntimeException("Animal with id = $animalId not found!")
    }

    @Synchronized
    suspend fun loadAnimal() {

        var numberOfAttempt = 0

        while (numberOfAttempt < MAX_ATTEMPTS_COUNT) {

            val exception = IOException("Can't load animal")

            try {
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

                addAnimalToOtherList(animal)
                break

            } catch (t: Throwable) {
                numberOfAttempt++
                Log.d("1", "loadAnimal: numberOfAttempt #$numberOfAttempt")
                exception.addSuppressed(t)
                if (numberOfAttempt == 2) {
                    throw exception
                }
            }



        }
        Log.d("1", "loadAnimal: $otherAnimalList")
    }

    private fun updateList() {
        animalLD.postValue(animalList.toList())
    }

    private fun clearList() {
        animalList.clear()
    }

}