package com.example.fitstream.presentation.splash_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitstream.R
import com.example.fitstream.databinding.FragmentSplashBinding
import com.example.fitstream.presentation.workout_screen.WorkoutFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAnimation()
    }

    private fun startAnimation() {
        binding.lottie.playAnimation()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2500)
            navigateToDetailFragment()
        }
    }

    private fun navigateToDetailFragment() {
        val action = SplashFragmentDirections.actionSplashToWorkout()
        findNavController().navigate(action)
    }
}