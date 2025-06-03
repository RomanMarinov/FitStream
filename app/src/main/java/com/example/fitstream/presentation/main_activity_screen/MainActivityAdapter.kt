package com.example.fitstream.presentation.main_activity_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitstream.databinding.ItemWorkoutBinding
import com.example.fitstream.domain.model.Workout

class MainActivityAdapter(
    private val onClickPlay: (id: Int) -> Unit
) : ListAdapter<Workout, MainActivityAdapter.ViewHolder>(DetailDiffUtilCallback()) {
    private var workouts: List<Workout> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ItemWorkoutBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(workouts[position])
    }

    override fun submitList(list: List<Workout>?) {
        super.submitList(list)
        list?.let { this.workouts = it.toList() }
    }

    inner class ViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(workout: Workout) {
            binding.tvTitle.text = workout.title
            binding.tvDescription.text = workout.description ?: "нет описания"
            binding.tvDuration.text = workout.duration.plus(" мин")
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