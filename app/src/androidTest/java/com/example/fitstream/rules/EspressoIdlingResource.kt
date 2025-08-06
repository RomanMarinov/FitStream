package com.example.fitstream.rules

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    val idlingResource = CountingIdlingResource("SplashDelay")
}