package com.tmvlg.zooanimal.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.data.repository.AnimalRepository

class AnimalDetailViewModel(
    private val repository: AnimalRepository
) : ViewModel() {

    private var _animal = MutableLiveData<Animal>()
    val animal = _animal.map { it }

    fun loadAnimal(animalId: Int) {
        val data = repository.getAnimal(animalId)
        _animal.postValue(data)
    }

}