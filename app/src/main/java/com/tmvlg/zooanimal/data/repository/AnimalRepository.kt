package com.tmvlg.zooanimal.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.local.db.AnimalDao
import com.tmvlg.zooanimal.data.network.AnimalZooApi
import com.tmvlg.zooanimal.data.network.models.AnimalResponse
import retrofit2.Response
import java.io.IOException
import java.lang.IndexOutOfBoundsException

class AnimalRepository(
    private val animalDao: AnimalDao
) {

    private val animalLD = MutableLiveData<List<Animal>>()

    private val animalList = mutableListOf<Animal>()

    private val otherAnimalList = mutableListOf<Animal>()

    private var autoIncrementId = 0

    private val MAX_ATTEMPTS_COUNT = 2

    private var sortable = false

    fun selectAnimal() {
        if (otherAnimalList.isNotEmpty()) {
            for (i in 0..animalList.size) {
                try {
                    val animal = otherAnimalList[i]
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
                catch (e: IndexOutOfBoundsException) {
                    break
                }
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
            val comparison = animal1.name.compareTo(animal2.name)
            comparison.compareTo(0)
        }
        updateList()
    }

    fun setNeedToSort(boolean: Boolean) {
        sortable = boolean
    }

    fun needToSort(): Boolean {
        return sortable
    }

    fun isListEmpty(): Boolean {
        return animalList.size > 0
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

    fun load10Animals() {

        val tempAnimalsList = mutableListOf<Animal>()

        try {

            val animalsResponse: Response<List<AnimalResponse>> = AnimalZooApi
                .retrofitService
                .get10Animals()
                .execute() ?: throw java.lang.RuntimeException("loading failed!")

            val animalsBody = animalsResponse.body()
                ?: throw java.lang.RuntimeException("loading failed!")

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
                animal.id = autoIncrementId++
                tempAnimalsList.add(animal)

            }
        } catch (t: Throwable) {
            Log.d("1", "load10Animals: can't")
        }
        animalDao.insert(tempAnimalsList)

    }

    fun setSavedAnimals() {
        clearList()
         animalDao.getLocalAnimals().forEach { animal ->
             if (animal.id == Animal.UNDEFINED_ID)
                 animal.id = autoIncrementId++
             animalList.add(animal)
         }
        updateList()

    }

    fun removeLocalAnimals() {
         animalDao.deleteLocal()
    }

    private fun updateList() {
        animalLD.postValue(animalList.toList())
    }

    private fun clearList() {
        animalList.clear()
    }

}