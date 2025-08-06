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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

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


        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )
        val workoutType = workouts
            .map { it.type }
            .distinct()
            .sorted()
            .plus(WorkoutType.ALL)


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

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value
        assertTrue(state is WorkoutUiState.Error)
        assertEquals(exception.message, WorkoutUiState.Error(message = "exception").message)
    }

    @Test
    fun `should update correctly state after filtering `() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        dispatcher.scheduler.advanceUntilIdle()

        val selectedType = WorkoutType.WORKOUT.description
        viewModel.filteringWorkoutsByType(selectedType)
        assertEquals(selectedType, savedStateHandle[Constants.WorkoutKeys.SELECTED_TYPE])

        val expectedFiltered: List<Workout> =
            workouts.filter { it.type.description == selectedType }
        val actualFiltered: List<Workout> =
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList()
        assertEquals(expectedFiltered, actualFiltered)

        val state = viewModel.workoutsState.value
        assertTrue(state is WorkoutUiState.Success)
        assertEquals(expectedFiltered, (state as WorkoutUiState.Success).workouts)

        val nonSelectedType = "NON_TYPE"
        viewModel.filteringWorkoutsByType(selectedType = nonSelectedType)
        val stateNon = viewModel.workoutsState.value
        assertTrue(stateNon is WorkoutUiState.Success)
        val actualNon = (stateNon as WorkoutUiState.Success).workouts
        assertEquals(workouts, actualNon)

        val act: List<Workout> = savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList()
        assertEquals(workouts, act)
    }

    @Test
    fun `should sorting list`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )

        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.workoutsState.value
        assertTrue(state is WorkoutUiState.Success)

        val expected = workouts
            .map { it.type }
            .sorted()
            .distinct()
            .plus(WorkoutType.ALL)
        val actual = (state as WorkoutUiState.Success).workoutsType
        assertEquals(expected, actual)
    }

    @Test
    fun `method eeee`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )

        dispatcher.scheduler.advanceUntilIdle()

        val textQuery = "Жир"
        viewModel.filteringWorkoutsByTitle(textQuery = textQuery)
        val actualTextQuery = savedStateHandle[Constants.WorkoutKeys.TEXT_QUERY] ?: ""
        assertEquals(textQuery, actualTextQuery)


        val filteringWorkoutsByTitle: List<Workout> = workouts.filter {
            it.title.contains(
                textQuery,
                ignoreCase = true
            )
        }
        val actualWorkoutByTitle: List<Workout> =
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS_BY_TITLE] ?: emptyList()
        assertEquals(filteringWorkoutsByTitle, actualWorkoutByTitle)


        val textQueryEmpty = ""
        viewModel.filteringWorkoutsByTitle(textQuery = textQueryEmpty)
        val actualTextQueryEmpty = savedStateHandle[Constants.WorkoutKeys.TEXT_QUERY] ?: "not_set"
        assertEquals(textQueryEmpty, actualTextQueryEmpty)

        val actualWorkoutEmpty: List<Workout> =
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS_BY_TITLE] ?: emptyList()
        assertEquals(emptyList<Workout>(), actualWorkoutEmpty)


        val state = viewModel.workoutsState.value
        assertTrue(state is WorkoutUiState.Success)
        val actual: List<Workout> = savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList()
        assertEquals(emptyList<Workout>(), actual)


        val actualAllWorkoutsSaved: List<Workout> =
            savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList()
        assertEquals(emptyList<Workout>(), actualAllWorkoutsSaved)
    }


    @Test
    fun `should save list`() = runTest {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.IO } returns dispatcher

        coEvery { workoutRepository.getWorkouts() } returns Result.success(workouts)

        viewModel = WorkoutViewModel(
            workoutRepository = workoutRepository,
            savedStateHandle = savedStateHandle
        )

        dispatcher.scheduler.advanceUntilIdle()

        viewModel.setWorkoutsState(workouts = workouts)

        val state = viewModel.workoutsState.value
        assertTrue(state is WorkoutUiState.Success)
        val savedList = savedStateHandle[Constants.WorkoutKeys.WORKOUTS] ?: emptyList<Workout>()

        assertTrue(savedList.isNotEmpty())
    }
}