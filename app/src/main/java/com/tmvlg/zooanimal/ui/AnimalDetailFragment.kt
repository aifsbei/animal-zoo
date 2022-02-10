package com.tmvlg.zooanimal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.tmvlg.zooanimal.R
import com.tmvlg.zooanimal.data.entities.Animal
import com.tmvlg.zooanimal.databinding.AnimalDetailFragmentBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AnimalDetailFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()

    private lateinit var viewModel: AnimalDetailViewModel

    private val animalDetailViewModelfactory by instance<AnimalDetailViewModelFactory>()

    private var _binding: AnimalDetailFragmentBinding? = null
    private val binding: AnimalDetailFragmentBinding
        get() = _binding ?: throw RuntimeException("null binding at $this")

    private var animalId: Int = Animal.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadState(requireArguments())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (savedInstanceState != null) {
            loadState(savedInstanceState)
        }

        _binding = AnimalDetailFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            animalDetailViewModelfactory
        )[AnimalDetailViewModel::class.java]

        viewModel.loadAnimal(animalId)

        observeViewModel()

        binding.returnButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun observeViewModel() {
        viewModel.animal.observe(viewLifecycleOwner) { animal ->
            Picasso.get()
                .load(animal.imageUrl)
                .resize(150, 150)
                .placeholder(R.drawable.placeholder)
                .into(binding.image)
            binding.latinName.text = animal.latinName
            binding.name.text = animal.name
            binding.activeTime.text = animal.activeTime
            binding.averageLength.text = animal.averageLength.toString()
            binding.averageWeight.text = animal.averageWeight.toString()
            binding.lifespan.text = animal.lifespan.toString()
            binding.habitat.text = animal.habitat
            binding.diet.text = animal.diet
            binding.geo.text = animal.geo
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    private fun saveState(outState: Bundle) {
        outState.putInt(EXTRAS_ANIMAL_ID, animalId)
    }

    private fun loadState(bundle: Bundle) {
        animalId = bundle.getInt(EXTRAS_ANIMAL_ID)
    }

    companion object {

        private const val EXTRAS_ANIMAL_ID = "extrasAnimalId"

        fun newInstance(animalId: Int): AnimalDetailFragment {
            return AnimalDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRAS_ANIMAL_ID, animalId)
                }
            }
        }
    }

}