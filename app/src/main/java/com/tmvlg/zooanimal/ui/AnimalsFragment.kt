package com.tmvlg.zooanimal.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tmvlg.zooanimal.R
import com.tmvlg.zooanimal.databinding.AnimalsFragmentBinding
import com.tmvlg.zooanimal.ui.animallist.AnimalListAdapter
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AnimalsFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private lateinit var viewModel: AnimalsViewModel

    private val animalFactory by instance<AnimalsViewModelFactory>()

    private var _binding: AnimalsFragmentBinding? = null
    private val binding: AnimalsFragmentBinding
        get() = _binding ?: throw RuntimeException("null binding at $this")

    private lateinit var animalAdapter: AnimalListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AnimalsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, animalFactory)[AnimalsViewModel::class.java]

        observeViewModel()

        val spanCount = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> 2
            Configuration.ORIENTATION_LANDSCAPE -> 4
            else -> 2
        }

        binding.animalRv.layoutManager = GridLayoutManager(requireContext(), spanCount)
        animalAdapter = AnimalListAdapter()
        binding.animalRv.adapter = animalAdapter


        binding.switchSortMethod.isChecked = viewModel.isNeedToSort()

        binding.switchSortMethod.setOnCheckedChangeListener { _, b ->
            if (b) {
                viewModel.sortAnimalList()
                viewModel.setSortAnyTime()
            } else {
                viewModel.stopSortingAnyTime()
            }
        }

        animalAdapter.onAnimalItemClickListener = { animal ->
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.main_container, AnimalDetailFragment.newInstance(animal.id))
                .addToBackStack(null)
                .commit()
        }

        animalAdapter.onDeleteAnimalClickListener = { animal ->
            viewModel.removeAnimalFromList(animal)
        }

    }

    private fun observeViewModel() {
        viewModel.animalList.observe(viewLifecycleOwner) {
            animalAdapter.submitList(it)
        }
        viewModel.loadingException.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Loading Error!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = AnimalsFragment()
    }

}