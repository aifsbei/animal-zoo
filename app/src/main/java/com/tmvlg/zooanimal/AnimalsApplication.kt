package com.tmvlg.zooanimal

import android.app.Application
import com.tmvlg.zooanimal.data.local.db.AnimalsDB
import com.tmvlg.zooanimal.data.repository.AnimalRepository
import com.tmvlg.zooanimal.ui.AnimalDetailViewModelFactory
import com.tmvlg.zooanimal.ui.AnimalsViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class AnimalsApplication : Application(), KodeinAware {
    override val kodein: Kodein = Kodein.lazy{
        import(androidXModule(this@AnimalsApplication))
        bind() from singleton { AnimalsDB(instance()) }
        bind() from singleton { AnimalsDB(instance()).animalDAO() }
        bind() from singleton { AnimalRepository(instance()) }
        bind() from provider { AnimalsViewModelFactory(instance()) }
        bind() from provider { AnimalDetailViewModelFactory(instance()) }
    }

}