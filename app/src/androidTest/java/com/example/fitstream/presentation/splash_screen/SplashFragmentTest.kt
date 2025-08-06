package com.example.fitstream.presentation.splash_screen

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fitstream.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SplashFragmentTest {

    @Test
    fun splashFragmentElementsAreDisplayed() {
        launchFragmentInContainer<SplashFragment>(
            themeResId = R.style.Theme_FitStream
        )

        onView(withId(R.id.lottie)).check(matches(isDisplayed()))
    }
}
