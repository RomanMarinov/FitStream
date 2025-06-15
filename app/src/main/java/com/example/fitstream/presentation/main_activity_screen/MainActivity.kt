package com.example.fitstream.presentation.main_activity_screen

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.example.fitstream.R
import com.example.fitstream.databinding.ActivityMainBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
//    private val viewModel: MainActivityViewModel by viewModels()
//    private lateinit var arrayAdapter: ArrayAdapter<Int>
//
//    private var allWorkouts = listOf<Workout>()
//    private var filterDropWorkouts: ArrayList<Workout>? = null
//
//    private var filterDropdownPosition = 0
//    private var etSearchList: ArrayList<Workout>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList("filterDropWorkouts", filterDropWorkouts)
//        outState.putInt("filterDropdownPosition", filterDropdownPosition)
//
//        outState.putParcelableArrayList("etSearchList", etSearchList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //initUI()
    }

//    private fun initUI() {
//        val mainActivityAdapter = MainActivityAdapter(viewModel::getVideWorkout)
//        binding.recyclerView.apply {
//            this.adapter = mainActivityAdapter
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        }
//
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.workoutsState.collect { state ->
//                    when(state) {
//                        WorkoutUiState.Empty -> {
//                            binding.progressBar.visibility = View.GONE
//                            binding.recyclerView.visibility = View.GONE
//                            binding.tvError.visibility = View.VISIBLE
//                            binding.btTryAgain.visibility = View.VISIBLE
//                            binding.tvError.text = getString(R.string.empty_list)
//                        }
//                        is WorkoutUiState.Error -> {
//                            binding.progressBar.visibility = View.GONE
//                            binding.recyclerView.visibility = View.GONE
//                            binding.tvError.visibility = View.VISIBLE
//                            binding.btTryAgain.visibility = View.VISIBLE
//                            binding.tvError.text = state.message
//                        }
//                        WorkoutUiState.Loading -> {
//                            binding.progressBar.visibility = View.VISIBLE
//                            binding.recyclerView.visibility = View.GONE
//                            binding.tvError.visibility = View.GONE
//                            binding.btTryAgain.visibility = View.GONE
//                        }
//                        is WorkoutUiState.Success -> {
//                            binding.progressBar.visibility = View.GONE
//                            binding.recyclerView.visibility = View.VISIBLE
//                            binding.tvError.visibility = View.GONE
//                            binding.btTryAgain.visibility = View.GONE
//                            allWorkouts = state.workouts
//
//                            val types = state.workouts.map { it.type }.distinct().sorted()
//                            if (filterDropWorkouts?.isNotEmpty() == true) {
//                                initAdapter(types = types)
//                                binding.filterDropdown.setText(filterDropdownPosition.toString(), false)
//                                mainActivityAdapter.submitList(filterDropWorkouts)
//                            } else if (!etSearchList.isNullOrEmpty()) {
//                                initAdapter(types = types)
//                                mainActivityAdapter.submitList(etSearchList)
//                            }
//                            else if (state.workouts.isNotEmpty()) {
//                                initAdapter(types = types)
//                                mainActivityAdapter.submitList(state.workouts)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        binding.btTryAgain.setOnClickListener {
//            viewModel.getWorkouts()
//        }
//
//        binding.filterDropdown.setOnItemClickListener { parent, _, position, _ ->
//            val selectedType = parent.getItemAtPosition(position) as Int
//            val filtered: List<Workout> = allWorkouts.filter { it.type == selectedType }
//            filterDropWorkouts = ArrayList(filtered)
//            filterDropdownPosition = selectedType
//
//            mainActivityAdapter.submitList(filtered)
//        }
//
//        viewModel.videWorkoutUIState.observe(this) { videWorkoutUIState ->
//            when(videWorkoutUIState) {
//                VideoUIState.Empty -> {
//                    binding.progressBar.visibility = View.GONE
//                    showToast(message = getString(R.string.no_data_try_again))
//                }
//                is VideoUIState.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    showToast(message = getString(R.string.error_try_again))
//                }
//                VideoUIState.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                }
//                is VideoUIState.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    val intent = Intent(this, DetailActivity::class.java)
//                    intent.putExtra("workout", videWorkoutUIState.workoutVideo)
//                    startActivity(intent)
//                }
//            }
//        }
//
//        binding.etSearch.addTextChangedListener { text ->
//            val textQuery = text.toString().trim()
//            if (textQuery.isEmpty()) {
//                binding.filterInputLayout.visibility = View.VISIBLE
//                if (filterDropWorkouts.isNullOrEmpty()) {
//                     mainActivityAdapter.submitList(allWorkouts)
//                }
//            } else {
//                filterDropWorkouts = null
//                filterDropdownPosition = 0
//                binding.filterDropdown.setText("", false)
//                binding.filterInputLayout.visibility = View.GONE
//                etSearchList = ArrayList(allWorkouts.filter { it.title.contains(textQuery, ignoreCase = true) })
//
//                if (!etSearchList.isNullOrEmpty()) {
//                    mainActivityAdapter.submitList(etSearchList)
//                }
//            }
//        }
//
//        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                // Скрыть клаву
//                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
//                binding.etSearch.clearFocus()
//                true
//            } else {
//                false
//            }
//        }
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun initAdapter(types: List<Int>) {
//        arrayAdapter = ArrayAdapter(
//            this@MainActivity, // контекст, внутри Activity
//            android.R.layout.simple_list_item_1, // стандартный layout
//            types
//        )
//
//        binding.filterDropdown.setAdapter(arrayAdapter)
//        val drawable =
//            ContextCompat.getDrawable(this@MainActivity, R.drawable.background_blue_rounded_white)
//        binding.filterDropdown.setDropDownBackgroundDrawable(drawable)
//    }
}