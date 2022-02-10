package com.tmvlg.zooanimal.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.network.AnimalZooApi
import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import retrofit2.Response
import java.io.IOException

object AnimalRepository {

    private val animalLD = MutableLiveData<List<Animal>>()

    private val animalList = mutableListOf<Animal>()

    private val otherAnimalList = mutableListOf<Animal>()

    private var autoIncrementId = 0

    private var isLoading = false

    private val MAX_ATTEMPTS_COUNT = 2

    private var sortable = false

    fun addAnimal() {
        if (otherAnimalList.isNotEmpty()) {
            while (true) {
                val animal = otherAnimalList.random()
                if (animalList.contains(animal)) {
                    continue
                }
                animalList.add((0..animalList.size).random(), animal)
                if (sortable) {
                    sortAnimals()
                } else {
                    updateList()
                }
                break
            }
        }
    }

    fun addAnimalToOtherList(animal: Animal) {
        if (animal.id == Animal.UNDEFINED_ID)
            animal.id = autoIncrementId++
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

    fun sortAnimals() {
        animalList.sortWith { animal1, animal2 ->
            val comparision =animal1.name.compareTo(animal2.name)
            comparision.compareTo(0)
        }
        updateList()
    }

    fun setNeedToSort(boolean: Boolean) {
        sortable = boolean
    }

    fun needToSort(): Boolean {
        return sortable
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
                exception.addSuppressed(t)
                if (numberOfAttempt == 2) {
                    throw exception
                }
            }



        }
    }

    private fun updateList() {
        animalLD.postValue(animalList.toList())
    }

    private fun clearList() {
        animalList.clear()
    }

}