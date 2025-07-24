package com.example.fitstream.presentation.workout_screen

import androidx.lifecycle.SavedStateHandle
import com.example.fitstream.domain.model.workout.Workout
import com.example.fitstream.domain.model.workout.WorkoutType
import com.example.fitstream.domain.repository.WorkoutRepository
import com.example.fitstream.presentation.util.Constants
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val workouts = listOf<Workout>(
    Workout(
        id = 1,
        title = "title1",
        description = "description1",
        type = WorkoutType.WORKOUT,
        duration = "duration1"
    ), Workout(
        id = 2,
        title = "title2",
        description = "description2",
        type = WorkoutType.STREAM,
        duration = "duration2"
    )
)

class WorkoutViewModelTest {

    private lateinit var viewModel: WorkoutViewModel
    private var workoutRepository = mockk<WorkoutRepository>()
    private val savedStateHandle = SavedStateHandle()
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher = dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should get workouts list for success result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )
        viewModel.getWorkouts()

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value

        assertTrue(state is WorkoutUiState.Success)
        assertEquals(
            workouts,
            WorkoutUiState.Success(workouts = workouts, workoutsType = emptyList()).workouts
        )
    }

    @Test
    fun `should get workoutType list for result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        val workoutType = workouts
            .map { it.type }
            .distinct()
            .sorted()
            .plus(WorkoutType.ALL)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )
        viewModel.getWorkouts()

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value

        assertTrue(state is WorkoutUiState.Success)
        assertEquals(workoutType, WorkoutUiState.Success(workouts, workoutType).workoutsType)
    }

    @Test
    fun `should get success empty list for result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val workoutsEmpty = emptyList<Workout>()
        coEvery { workoutRepository.getWorkouts() } returns Result.success(workoutsEmpty)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )
        viewModel.getWorkouts()

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value

        assertTrue(state is WorkoutUiState.Empty)
        assertEquals(state, WorkoutUiState.Empty)
    }

    @Test
    fun `should get failure for result`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        val exception = Exception("exception")
        coEvery { workoutRepository.getWorkouts() } returns Result.failure(exception)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )
        viewModel.getWorkouts()

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value

        assertTrue { state is WorkoutUiState.Error }
        assertEquals(exception.message, WorkoutUiState.Error(message = "exception").message)
    }

    @Test
    fun `should ` () = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle= savedStateHandle
        )

        viewModel.getWorkouts()

        dispatcher.scheduler.advanceUntilIdle()

        val selectedType = WorkoutType.WORKOUT.description
        viewModel.filteringWorkoutsByType(selectedType)


        val state = viewModel.workoutsState.value
        assertTrue { state is WorkoutUiState.Success }

        val filteredWorkout = workouts.filter { it.description == selectedType }
        assertEquals(filteredWorkout, (state as WorkoutUiState.Success).workouts)

       // val savedWorkout = savedStateHandle[Constants.WorkoutKeys.WORKOUTS]
    }
}