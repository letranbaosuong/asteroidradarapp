package com.letranbaosuong.asteroidradarapp.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.letranbaosuong.asteroidradarapp.R
import com.letranbaosuong.asteroidradarapp.databinding.FragmentMainBinding

@RequiresApi(Build.VERSION_CODES.M)
@Suppress("DEPRECATION")
class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)

        val adapter = AsteroidItemAdapter(AsteroidItemClick {
            findNavController().navigate(
                MainFragmentDirections.actionShowDetail(it)
            )
        })
        binding.asteroidRecycler.adapter = adapter
//            binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())
        mainViewModel.listAsteroid.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("true"))
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        mainViewModel.onMenuSelected(item)
        return true
    }
}
