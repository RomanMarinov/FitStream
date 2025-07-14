package com.example.fitstream.presentation.detail_screen

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.example.fitstream.domain.repository.DetailRepository
import io.mockk.mockk

 class DetailViewModelTest {

   private lateinit var viewModel: DetailViewModel
   private lateinit var context: Context
   private lateinit var savedStateHandle: SavedStateHandle
   private val detailRepository = mockk<DetailRepository>()

}