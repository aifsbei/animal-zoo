package com.tmvlg.zooanimal.ui.animallist

import androidx.recyclerview.widget.DiffUtil
import com.tmvlg.zooanimal.data.entities.Animal

class AnimalDiffCallback : DiffUtil.ItemCallback<Animal>() {

    override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
        return oldItem == newItem
    }
}