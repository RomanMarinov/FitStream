package com.example.fitstream.presentation.detail_screen

import androidx.lifecycle.SavedStateHandle
import com.example.fitstream.domain.model.detail.Detail
import com.example.fitstream.domain.repository.DetailRepository
import com.example.fitstream.utils.resource.ResourceProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private var detailRepository = mockk<DetailRepository>()
    private var resourceProvider = mockk<ResourceProvider>()
    private var savedStateHandle = SavedStateHandle()
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
        viewModel = DetailViewModel(
            detailRepository = detailRepository,
            savedStateHandle = savedStateHandle,
            resourceProvider = resourceProvider
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get success result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val detail = Detail(id = 0, duration = "duration", link = "link")

        coEvery { detailRepository.getVideoWorkout(id = 1) } returns Result.success(detail)

        viewModel.getVideoWorkout(id = 1)

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.videoWorkoutUIState.value
        assertTrue(state is VideoUIState.Success)
        assertEquals(expected = "link", actual = state.workoutVideo.link)
    }

    @Test
    fun `should get failure result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val exception = Exception("exception failure")
        coEvery { detailRepository.getVideoWorkout(id = 1) } returns Result.failure(exception)

        viewModel.getVideoWorkout(id = 1)

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.videoWorkoutUIState.value

        assertTrue(state is VideoUIState.Error)

        assertEquals(expected = "exception failure", actual = state.message)
    }

    @Test
    fun `should save success for currentPlayback values `() {
        val currentPlayback = CurrentPlayback(
            position = 0L,
            playWhenReady = true
        )

        viewModel.setCurrentPlayback(currentPlayback = currentPlayback)

        val expectedCurrentPlayback = CurrentPlayback(
            position = 0L,
            playWhenReady = true
        )

        val actualCurrentPlayback = CurrentPlayback(
            position = viewModel.position,
            playWhenReady = viewModel.playWhenReady
        )

        assertEquals(expectedCurrentPlayback, actualCurrentPlayback)
    }

    @Test
    fun `should save success for playingState value`() {
        val isPlayingState = false

        viewModel.setPlayingState(isPlaying = isPlayingState)

        val expected = false
        val actual = viewModel.isPlaying

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun `should return default value for empty exception`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val messageException =
            Exception(com.example.fitstream.data.util.Constants.Detail.EMPTY_RESPONSE)
        coEvery { detailRepository.getVideoWorkout(1) } returns Result.failure(messageException)

        viewModel.getVideoWorkout(1)

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.videoWorkoutUIState.value

        assertTrue(state is VideoUIState.Error)
        assertEquals(messageException.message, state.message)
    }

    @Test
    fun `should return default error value for exception`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val messageException =
            Exception(com.example.fitstream.data.util.Constants.Detail.ERROR_RESPONSE)
        coEvery { detailRepository.getVideoWorkout(1) } returns Result.failure(messageException)

        viewModel.getVideoWorkout(1)

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.videoWorkoutUIState.value

        assertTrue(state is VideoUIState.Error)
        assertEquals(messageException.message, state.message)
    }
}