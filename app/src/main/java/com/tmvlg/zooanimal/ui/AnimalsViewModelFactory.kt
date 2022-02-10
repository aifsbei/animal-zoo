package com.tmvlg.zooanimal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tmvlg.zooanimal.data.repository.AnimalRepository
import java.lang.RuntimeException

class AnimalsViewModelFactory(
    private val repository: AnimalRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalsViewModel::class.java))
            return AnimalsViewModel(repository) as T
        throw RuntimeException("Unknown view model class $modelClass")
    }
}