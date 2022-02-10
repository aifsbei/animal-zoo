package com.tmvlg.zooanimal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.repository.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class AnimalsViewModel(
    private val repository: AnimalRepository
) : ViewModel() {
    val animalList = repository.getAnimalList()

    private var _loadingException = MutableLiveData<IOException?>()
    val loadingException = _loadingException.map { it }

    fun startLoadingAnimals() = viewModelScope.launch(Dispatchers.IO) {
        while (_loadingException.value == null) {
            try {
//                val animal1 = async {repository.loadAnimal() }
//                val animal2 = async {repository.loadAnimal() }
//                animal1.await()
//                animal2.await()
                repeat(2) {
                    repository.loadAnimal()
                }

            } catch (e: IOException) {
                _loadingException.postValue(e)
            }

            delay(LOADING_DELAY_MS)
        }
    }


    fun startShowingAnimal() = viewModelScope.launch {
        while (_loadingException.value == null) {
            repository.addAnimal()
            delay(APPEARING_DELAY_MS)
        }

    }

    fun removeAnimalFromList(animal: Animal) {
        repository.deleteAnimal(animal)
    }

    fun sortAnimalList() {
        repository.sortAnimals()
    }

    fun setSortAnyTime() {
        repository.setNeedToSort(true)
    }

    fun stopSortingAnyTime() {
        repository.setNeedToSort(false)
    }

    fun isNeedToSort(): Boolean {
        return repository.needToSort()
    }

    companion object {
        private const val LOADING_DELAY_MS = 3000L
        private const val APPEARING_DELAY_MS = 5000L
    }

    init {
        startLoadingAnimals()
        startShowingAnimal()
    }

}