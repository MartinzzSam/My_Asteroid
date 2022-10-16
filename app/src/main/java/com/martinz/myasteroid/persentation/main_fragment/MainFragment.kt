package com.martinz.myasteroid.persentation.main_fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.martinz.myasteroid.R
import com.martinz.myasteroid.data.model.PictureOfDay
import com.martinz.myasteroid.databinding.FragmentMainBinding
import com.martinz.myasteroid.persentation.AsteroidAdapter
import com.martinz.myasteroid.persentation.AsteroidEvent
import com.martinz.myasteroid.persentation.OnClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {
    lateinit var asteroidAdapter: AsteroidAdapter
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        asteroidAdapter = AsteroidAdapter(OnClickListener { asteroid ->
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDetailFragment(
                    asteroid
                )
            )

        })

        binding.apply {
            asteroidRecycler.apply {
                adapter = asteroidAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        lifecycleScope.launchWhenCreated {
            loadingObserver()
        }
        statusObserver()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.view_week_asteroids -> {
                Toast.makeText(context, "Week Asteroids", Toast.LENGTH_SHORT).show()
                viewModel.onEvent(AsteroidEvent.GetWeekAsteroid)
            }
            R.id.view_today_asteroids -> {
                Toast.makeText(context, "Today's Asteroids", Toast.LENGTH_SHORT).show()
               viewModel.onEvent(AsteroidEvent.GetTodayAsteroid)
            }
            R.id.view_saved_asteroids -> {
                Toast.makeText(context, "Saved Asteroids", Toast.LENGTH_SHORT).show()
                viewModel.onEvent(AsteroidEvent.GetSavedAsteroid)
            }
        }
        return true
    }


    private fun statusObserver() {
        viewModel.liveData.observe(viewLifecycleOwner) {
            updatePictureOfDay(it.pictureOfDay)
            asteroidAdapter.submitList(it.asteroids)
            binding.asteroidRecycler.post {
                binding.asteroidRecycler.smoothScrollToPosition(0)
            }

        }
    }

    private suspend fun loadingObserver() {
        viewModel.isLoading.collect() { isLoading ->
            when (isLoading) {
                true -> {
                    binding.statusLoadingWheel.visibility = View.VISIBLE
                }
                false -> {
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }
        }
    }


    private fun updatePictureOfDay(pictureOfDay: PictureOfDay?) {
        if (pictureOfDay != null) {
            binding.pictureOfDay = pictureOfDay
        }

    }
}
