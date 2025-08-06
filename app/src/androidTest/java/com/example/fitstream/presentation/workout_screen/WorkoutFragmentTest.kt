package com.example.fitstream.presentation.workout_screen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.fitstream.R
import com.example.fitstream.presentation.main_activity_screen.MainActivity
import com.example.fitstream.rules.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class WorkoutFragmentTest {

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }


    @Test
    fun workoutFragmentElementsAreDisplayed() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.searchInputLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.filterInputLayout)).check(matches(isDisplayed()))

        waitForViewToBeDisplayed(withId(R.id.recyclerWorkouts), timeoutMs = 5000)
//        onView(withId(R.id.recyclerWorkouts)).check(matches(isDisplayed()))
    }
}