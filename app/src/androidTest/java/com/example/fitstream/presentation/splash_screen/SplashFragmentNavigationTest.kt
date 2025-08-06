package com.example.fitstream.presentation.splash_screen

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitstream.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SplashFragmentNavigationTest {

    @Test
    fun splashFragment_navigatesToWorkoutFragment_afterDelay() {
        val mockNavController = mock<NavController>()

        val scenario = launchFragmentInContainer<SplashFragment>(
            themeResId = R.style.Theme_FitStream
        )

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
        }

        // Реальное ожидание (не круто, но работает)
        Thread.sleep(3000)

        verify(mockNavController).navigate(
            SplashFragmentDirections.actionSplashToWorkout()
        )
    }
}
