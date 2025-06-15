package com.example.fitstream.presentation.detail_screen

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.navigation.fragment.navArgs
import com.example.fitstream.BuildConfig
import com.example.fitstream.R
import com.example.fitstream.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@UnstableApi
@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var exoPlayer: ExoPlayer
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor()
        initUI()
    }

    @OptIn(UnstableApi::class)
    private fun initUI() {
        getVideWorkout()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoWorkoutUIState.collect { videoWorkoutUIState ->
                    when (videoWorkoutUIState) {
                        VideoUIState.Empty -> {
                            binding.btVideoTryAgain.visibility = View.VISIBLE
                            showToast(message = getString(R.string.no_data_try_again))
                        }

                        is VideoUIState.Error -> {
                            binding.btVideoTryAgain.visibility = View.VISIBLE
                            showToast(message = getString(R.string.error_try_again))
                        }

                        VideoUIState.Loading -> {
                            binding.btVideoTryAgain.visibility = View.GONE
                        }

                        is VideoUIState.Success -> {
                            binding.btVideoTryAgain.visibility = View.GONE
                            val videoLink = videoWorkoutUIState.workoutVideo.link
                            initPlayer(videoLink = videoLink)
                        }
                    }
                }
            }
        }

        binding.btVideoTryAgain.setOnClickListener {
            getVideWorkout()
        }
    }

    @OptIn(UnstableApi::class)
    fun initPlayer(videoLink: String) {
        val trackSelector = DefaultTrackSelector(requireContext()).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        exoPlayer = ExoPlayer
            .Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build()
        val videoUri = BuildConfig.BASE_URL.plus(videoLink)
        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.seekTo(viewModel.position)
        exoPlayer.playWhenReady = viewModel.playWhenReady
        binding.player.player = exoPlayer
    }

    private fun getVideWorkout() {
        viewModel.getVideWorkout(id = args.id)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun changeStatusBarColor() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = 0
        }
    }

    @OptIn(UnstableApi::class)
    override fun onStop() {
        super.onStop()
        viewModel.setPlaybackState(
            position = exoPlayer.currentPosition,
            playWhenReady = exoPlayer.playWhenReady
        )
        exoPlayer.pause()
    }

    @UnstableApi
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}