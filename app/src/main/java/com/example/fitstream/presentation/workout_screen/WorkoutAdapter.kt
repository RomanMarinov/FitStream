package com.example.fitstream.presentation.workout_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitstream.databinding.ItemWorkoutBinding
import com.example.fitstream.domain.model.workout.Workout

class WorkoutAdapter(
    private val onClickPlay: (id: Int) -> Unit
) : ListAdapter<Workout, WorkoutAdapter.ViewHolder>(DetailDiffUtilCallback()) {

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
        fun bind(workout: Workout) {
            binding.tvTitle.text = workout.title

            if (workout.description == null) {
                binding.tvDescription.visibility = View.GONE
                binding.lottieLoadingDesc.visibility = View.VISIBLE
                binding.lottieLoadingDesc.playAnimation()
            } else {
                binding.lottieLoadingDesc.visibility = View.GONE
                binding.lottieLoadingDesc.cancelAnimation()
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescription.text = workout.description
            }

            val durationInt = workout.duration.toIntOrNull()
            if (durationInt != null) {
                binding.tvDuration.text = workout.duration.plus(" мин")
                binding.tvDuration.visibility = View.VISIBLE
                binding.lottieLoadingMin.visibility = View.GONE
                binding.lottieLoadingMin.cancelAnimation()
            } else {
                binding.tvDuration.visibility = View.GONE
                binding.lottieLoadingMin.visibility = View.VISIBLE
                binding.lottieLoadingMin.playAnimation()
            }

            binding.cardView.setOnClickListener {
                onClickPlay(workout.id)
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