package com.letranbaosuong.asteroidradarapp.main

import AsteroidItemAdapter
import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letranbaosuong.asteroidradarapp.R
import com.letranbaosuong.asteroidradarapp.database.AsteroidDatabase
import com.letranbaosuong.asteroidradarapp.databinding.FragmentMainBinding
import com.letranbaosuong.asteroidradarapp.models.Asteroid

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        val dataList = listOf(
            Asteroid(0, "name", "", 0.0, 0.0, 0.0, 0.0, true),
            Asteroid(0, "name", "", 0.0, 0.0, 0.0, 0.0, true),
        )
        val adapter = AsteroidItemAdapter(requireContext(), dataList)
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

//        val dataSource =
//            AsteroidDatabase.getDatabaseInstance(requireContext()).asteroidDatabaseDao
        val viewModelFactory = MainViewModelFactory(requireActivity().application)
        val mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
//        mainViewModel.viewModelScope

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
