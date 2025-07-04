package com.example.fitstream.presentation.workout_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitstream.databinding.ItemWorkoutBinding
import com.example.fitstream.domain.model.workout.Workout

class WorkoutsAdapter(
    private val onClickPlay: (id: Int, desc: String) -> Unit
) : ListAdapter<Workout, WorkoutsAdapter.ViewHolder>(DetailDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ItemWorkoutBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: Workout) = with(binding) {
            tvTitle.text = workout.title
            tvDescription.text = workout.description ?: ""

            val durationInt = workout.duration.toIntOrNull()
            tvDuration.text = if (durationInt != null) {
                workout.duration.plus(" мин")
            } else {
                ""
            }

            cardView.setOnClickListener {
                onClickPlay(workout.id, workout.description ?: "")
            }
        }
    }
}

class DetailDiffUtilCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(
        oldItem: Workout,
        newItem: Workout
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Workout,
        newItem: Workout
    ): Boolean {
        return oldItem == newItem
    }
}