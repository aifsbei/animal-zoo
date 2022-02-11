package com.tmvlg.zooanimal.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tmvlg.zooanimal.R
import com.tmvlg.zooanimal.databinding.AnimalsFragmentBinding
import com.tmvlg.zooanimal.ui.animallist.AnimalListAdapter
import com.tmvlg.zooanimal.util.isOnline
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



        if (!requireContext().isOnline()) {
            if (!viewModel.isAlreadyLoaded()) {
                viewModel.initOfflineMode()
            }
            binding.noInternetLayout.visibility = View.VISIBLE
        } else {
            viewModel.saveSomeAnimals()
//            viewModel.startViewModelThreads()
        }

        binding.switchSortMethod.isChecked = viewModel.isNeedToSort()

        binding.switchSortMethod.setOnCheckedChangeListener { _, b ->
            if (b) {
                viewModel.sortAnimalList()
                viewModel.setSortAnyTime()
            } else {
                viewModel.stopSortingAnyTime()
            }
        }

        binding.retryButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, AnimalsFragment.newInstance())
                .commit()
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
//            Toast.makeText(requireContext(), "Loading Error!", Toast.LENGTH_SHORT).show()
//            val contextView = requireActivity().findViewById<View>(R.id.animals_fragment_container)
            binding.noInternetLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = AnimalsFragment()
    }

}