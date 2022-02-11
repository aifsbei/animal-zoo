package com.tmvlg.zooanimal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.repository.AnimalRepository
import kotlinx.coroutines.*
import java.io.IOException

class AnimalsViewModel(
    private val repository: AnimalRepository
) : ViewModel() {
    val animalList = repository.getAnimalList()

    private var _loadingException = MutableLiveData<IOException?>()
    val loadingException = _loadingException.map { it }

    fun startLoadingAnimals() = viewModelScope.launch(Dispatchers.IO) {
        supervisorScope {
            while (_loadingException.value == null) {
                try {

                    val animal1 = async {repository.loadAnimal() }
                    val animal2 = async {repository.loadAnimal() }
                    animal1.await()
                    animal2.await()

                } catch (e: IOException) {
                    _loadingException.postValue(e)
                }

                delay(LOADING_DELAY_MS)
            }
        }
    }


    fun startShowingAnimal() = viewModelScope.launch {
        while (_loadingException.value == null) {
            repository.selectAnimal()
            delay(APPEARING_DELAY_MS)
        }

    }

    fun startViewModelThreads() {
        startLoadingAnimals()
        startShowingAnimal()
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

    fun saveSomeAnimals() = viewModelScope.launch(Dispatchers.IO) {

        // make exception reusable
        if (_loadingException.value != null)
            _loadingException.postValue(null)

        repository.removeLocalAnimals()
        repository.load10Animals()
    }

    fun initOfflineMode() = viewModelScope.launch(Dispatchers.IO) {
        repository.setSavedAnimals()
    }

    fun isAlreadyLoaded(): Boolean {
        return repository.isListEmpty()
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