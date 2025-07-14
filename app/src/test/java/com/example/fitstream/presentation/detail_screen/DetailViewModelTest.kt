//package com.example.fitstream.presentation.detail_screen
//
//import android.content.Context
//import androidx.lifecycle.SavedStateHandle
//import com.example.fitstream.domain.repository.DetailRepository
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//
////import org.junit.jupiter.api.Assertions.*
//class DetailViewModelTest {
//
//    private lateinit var viewModel: DetailViewModel
//    private var detailRepository = mockk<DetailRepository>()
//    private lateinit var context: Context
//    private lateinit var savedStateHandle: SavedStateHandle
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(UnconfinedTestDispatcher())
//        viewModel = DetailViewModel(
//            detailRepository = detailRepository,
//            savedStateHandle = savedStateHandle,
//            context = mockk(relaxed = true)
//        )
//    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun ``
//
////@org.junit.jupiter.api.Test
//// fun isPlaying() {}
////
////@org.junit.jupiter.api.Test
//// fun getPosition() {}
////
////@org.junit.jupiter.api.Test
//// fun getPlayWhenReady() {}
////
////@org.junit.jupiter.api.Test
//// fun getVideoWorkoutUIState() {}
////
////@org.junit.jupiter.api.Test
//// fun getVideoWorkout() {}
////
////@org.junit.jupiter.api.Test
//// fun setCurrentPlayback() {}
////
////@org.junit.jupiter.api.Test
//// fun setPlayingState() {}
//}