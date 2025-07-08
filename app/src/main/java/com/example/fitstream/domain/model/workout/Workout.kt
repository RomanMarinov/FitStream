package com.example.fitstream.domain.model.workout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    val id: Int,
    val title: String,
    val description: String?,
    val type: WorkoutType,
    val duration: String
) : Parcelable

enum class WorkoutType(val id: Int, val description: String) {
    WORKOUT(1, "Тренировка"),
    STREAM(2, "Эфир"),
    COMPLEX(3, "Комплекс"),
    ALL(4, "Все");

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id }
    }
}
