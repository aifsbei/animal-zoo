package com.tmvlg.zooanimal.ui.animallist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.databinding.AnimalListItemBinding

class AnimalListAdapter: ListAdapter<Animal, AnimalViewHolder>(AnimalDiffCallback())  {

    var onAnimalItemClickListener: ((Animal) -> Unit)? = null
    var onDeleteAnimalClickListener: ((Animal) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val binding = AnimalListItemBinding.inflate(inflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animalItem = getItem(position)

        with(holder) {
            binding.animalName.text = animalItem.name
            Picasso.get()
                .load(animalItem.imageUrl)
                .resize(24, 24)
                .into(binding.animalImage)

            binding.animalLayout.setOnClickListener {
                onAnimalItemClickListener?.invoke(animalItem)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteAnimalClickListener?.invoke(animalItem)
            }
        }
    }
}