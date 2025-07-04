package com.example.fitstream.presentation.detail_screen

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import androidx.navigation.fragment.navArgs
import com.example.fitstream.BuildConfig
import com.example.fitstream.R
import com.example.fitstream.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@UnstableApi
@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var exoPlayer: ExoPlayer
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var buttonPlay: ImageButton
    private lateinit var buttonPause: ImageButton

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
        Log.d("FragmentCheck", "onCreateView called")

        initPlayerButtons()

        initUI()
    }

    private fun initPlayerButtons() {
        buttonPlay = binding.player.findViewById(R.id.exo_play)
        buttonPause = binding.player.findViewById(R.id.exo_pause)
    }


    @OptIn(UnstableApi::class)
    private fun initUI() {
        getVideWorkout()

        viewLifecycleOwner.lifecycleScope.launch {
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

        binding.player.setControllerHideOnTouch(true)
        binding.player.showController()

        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                viewModel.setPlayingState(isPlaying = isPlaying)
                if (isPlaying) {
                    binding.btnPlay.visibility = View.GONE
                    binding.btnPause.visibility = View.VISIBLE
                } else {
                    if (exoPlayer.playbackState == Player.STATE_ENDED) {
                        binding.btnPlay.visibility = View.VISIBLE
                        binding.btnPause.visibility = View.GONE
                    } else {
                        binding.btnPlay.visibility = View.VISIBLE
                        binding.btnPause.visibility = View.GONE
                    }
                }
            }
        })

        buttonPlay.setOnClickListener {
            if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            } else {
                exoPlayer.play()
            }
        }
        buttonPause.setOnClickListener {
            exoPlayer.pause()
        }

        binding.btnRewind.setOnClickListener {
            val currentPosition = exoPlayer.currentPosition
            val position = (currentPosition - 5000L).coerceAtLeast(0L)
            exoPlayer.seekTo(position)
        }

        binding.btnForward.setOnClickListener {
            val currentPosition = exoPlayer.currentPosition
            val duration = exoPlayer.duration
            val position = (currentPosition + 5000L).coerceAtMost(duration)
            exoPlayer.seekTo(position)
        }

        updatePlayerLayoutForOrientation()

        binding.btnPlay.setOnClickListener {
            if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            } else {
                exoPlayer.play()
            }
        }

        binding.btnPause.setOnClickListener {
            exoPlayer.pause()
        }

        setControllerPlayerVisibility()
    }

    private fun setControllerPlayerVisibility() {
        binding.player.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
            binding.btnRewind.visibility = visibility
            binding.btnForward.visibility = visibility
            if (viewModel.isPlaying) {
                binding.btnPause.visibility = visibility
            } else {
                binding.btnPlay.visibility = visibility
            }
        })
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

    private fun updatePlayerLayoutForOrientation() {
        val orientation = resources.configuration.orientation
        val layoutParams = binding.player.layoutParams as ConstraintLayout.LayoutParams

        binding.tvDesc.text = args.desc ?: ""

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.tvDesc.visibility = View.GONE
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        } else {
            binding.tvDesc.visibility = View.VISIBLE
            layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_portrait_player)
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        }
        binding.player.layoutParams = layoutParams
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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}