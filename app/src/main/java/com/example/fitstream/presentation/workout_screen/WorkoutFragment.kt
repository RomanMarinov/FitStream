package com.example.fitstream.presentation.workout_screen

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitstream.R
import com.example.fitstream.databinding.FragmentWorkoutBinding
import com.example.fitstream.domain.model.workout.WorkoutType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WorkoutFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutBinding
    private val viewModel: WorkoutViewModel by viewModels()


    private lateinit var arrayAdapter: ArrayAdapter<String>

//    private var allWorkouts = listOf<Workout>()
//    private var filterDropWorkouts: ArrayList<Workout>? = null
//
//    private var filterDropdownPositionDescription = ""
//    private var etSearchList: ArrayList<Workout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        val workoutAdapter = WorkoutAdapter(onClickPlay = { workoutId ->
            navigateToDetailFragment(id = workoutId)
        })
        binding.recyclerWorkouts.apply {
            this.adapter = workoutAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.workoutsState.collect { state ->
                    when (state) {
                        WorkoutUiState.Empty -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerWorkouts.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.btTryAgain.visibility = View.VISIBLE
                            binding.tvError.text = getString(R.string.empty_list)
                        }

                        is WorkoutUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerWorkouts.visibility = View.GONE
                            binding.tvError.visibility = View.VISIBLE
                            binding.btTryAgain.visibility = View.VISIBLE
                            binding.tvError.text = state.message
                        }

                        WorkoutUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerWorkouts.visibility = View.GONE
                            binding.tvError.visibility = View.GONE
                            binding.btTryAgain.visibility = View.GONE
                        }

                        is WorkoutUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerWorkouts.visibility = View.VISIBLE
                            binding.tvError.visibility = View.GONE
                            binding.btTryAgain.visibility = View.GONE
                            viewModel.setWorkoutsState(workouts = state.workouts)

                            initAdapter(types = state.workoutsType)
                            workoutAdapter.submitList(state.workouts)
                        }
                    }
                }
            }
        }

        binding.btTryAgain.setOnClickListener {
            viewModel.getWorkouts()
        }


        binding.filterDropdown.setOnItemClickListener { parent, _, position, _ ->
            val selectedType = parent.getItemAtPosition(position) as String
            viewModel.filteringWorkoutsByType(selectedType = selectedType)
        }

        binding.etSearch.addTextChangedListener { text ->
            val textQuery = text.toString().trim()
            viewModel.filteringWorkoutsByTitle(textQuery = textQuery)
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }
    }

    private fun initAdapter(types: List<WorkoutType?>) {

        val typesWithAll = mutableListOf<WorkoutType?>()
        typesWithAll.addAll(types)
        typesWithAll.add(WorkoutType.ALL)

        Log.d("4444", " typesWithAll=" + typesWithAll)
        val description: List<String> = typesWithAll.map { it?.description ?: "" }
        arrayAdapter = ArrayAdapter(
            requireContext(), // контекст, внутри Activity
            android.R.layout.simple_list_item_1, // стандартный layout
            description
        )

        binding.filterDropdown.setAdapter(arrayAdapter)
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.background_blue_rounded_white)
        binding.filterDropdown.setDropDownBackgroundDrawable(drawable)
    }

    private fun navigateToDetailFragment(id: Int) {
        val action = WorkoutFragmentDirections.actionWorkoutToDetail(id)
        findNavController().navigate(action)
    }
}