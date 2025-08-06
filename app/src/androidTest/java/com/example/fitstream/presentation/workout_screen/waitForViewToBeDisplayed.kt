package com.example.fitstream.presentation.workout_screen

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

fun waitForViewToBeDisplayed(matcher: Matcher<View>, timeoutMs: Long = 5000) {
    val startTime = System.currentTimeMillis()
    val endTime = startTime + timeoutMs
    var lastError: Throwable? = null

    do {
        try {
            onView(matcher).check(matches(isDisplayed()))
            return
        } catch (e: Throwable) {
            lastError = e
            Thread.sleep(100)
        }
    } while (System.currentTimeMillis() < endTime)

    throw lastError
}