package com.example.fitstream.presentation.detail_screen

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
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
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.navigation.fragment.navArgs
import com.example.fitstream.App
import com.example.fitstream.R
import com.example.fitstream.databinding.FragmentDetailBinding
import com.example.fitstream.di.factory.detail_viewmodel.DetailViewModelFactoryProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailFragment : Fragment() {

    @UnstableApi
    @Inject
    lateinit var exoPlayerFacade: ExoPlayerFacade

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding

    @Inject
    lateinit var viewModelFactoryProvider: DetailViewModelFactoryProvider
    private val viewModel: DetailViewModel by viewModels {
        viewModelFactoryProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().applicationContext as App)
            .appComponent.inject(this)
    }

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

    private fun initUI() {
        getVideoWorkout()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.videoWorkoutUIState.collect { videoWorkoutUIState ->
                    when (videoWorkoutUIState) {
                        VideoUIState.Empty -> setVideoUIStateEmpty()
                        is VideoUIState.Error -> setVideoUIStateError(videoWorkoutUIState = videoWorkoutUIState)
                        VideoUIState.Loading -> setVideoUIStateLoading()
                        is VideoUIState.Success -> setVideoUIStateSuccess(videoWorkoutUIState = videoWorkoutUIState)
                    }
                }
            }
        }

        binding.btVideoTryAgain.setOnClickListener {
            getVideoWorkout()
        }
    }

    private fun setVideoUIStateEmpty() {
        binding.btVideoTryAgain.visibility = View.VISIBLE
        showToast(message = getString(R.string.no_data_try_again))
    }

    private fun setVideoUIStateError(videoWorkoutUIState: VideoUIState.Error) {
        binding.btVideoTryAgain.visibility = View.VISIBLE


        showToast(message = getString(R.string.error_try_again))
    }

    private fun setVideoUIStateLoading() {
        binding.btVideoTryAgain.visibility = View.GONE
    }

    private fun setVideoUIStateSuccess(videoWorkoutUIState: VideoUIState.Success) {
        binding.btVideoTryAgain.visibility = View.GONE
        val videoLink = videoWorkoutUIState.workoutVideo.link
        initPlayer(videoLink = videoLink)
    }

    private fun initPlayer(videoLink: String) {
        setExoPlayer(videoLink = videoLink)
        setObserverExoPlayer()
        setButtonsClickListener()
        setControllerPlayerVisibility()
    }

    @OptIn(UnstableApi::class)
    private fun setExoPlayer(videoLink: String) {
        val exoPlayer = exoPlayerFacade.initExoPlayer(
            videoLink = videoLink,
            position = viewModel.position,
            playWhenReady = viewModel.playWhenReady
        )
        binding.player.player = exoPlayer
        binding.player.setControllerHideOnTouch(true)
        binding.player.showController()
    }

    private fun setObserverExoPlayer() {
        exoPlayerFacade.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            viewModel.setPlayingState(isPlaying = isPlaying)
            updatePlayPauseUI(isPlaying, exoPlayerFacade.getPlaybackState())
        }
    }

    private fun setButtonsClickListener() {
        binding.btnRewind.setOnClickListener {
            exoPlayerFacade.rewind()
        }

        binding.btnForward.setOnClickListener {
            exoPlayerFacade.forward()
        }

        updatePlayerLayoutForOrientation()

        binding.btnPlay.setOnClickListener {
            exoPlayerFacade.playOrRestartIfEnd()
        }

        binding.btnPause.setOnClickListener {
            exoPlayerFacade.pause()
        }
    }

    private fun updatePlayPauseUI(isPlaying: Boolean, playbackState: Int) {
        val shouldShowPlay = playbackState == Player.STATE_ENDED || !isPlaying
        binding.btnPlay.visibility = if (shouldShowPlay) View.VISIBLE else View.GONE
        binding.btnPause.visibility = if (shouldShowPlay) View.GONE else View.VISIBLE
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

    private fun getVideoWorkout() {
        viewModel.getVideoWorkout(id = args.id)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun changeStatusBarColor() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)
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

    override fun onStop() {
        super.onStop()
        val currentPlayback = exoPlayerFacade.getCurrentPlayBack()
        viewModel.setCurrentPlayback(currentPlayback)
        exoPlayerFacade.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayerFacade.releasePlayer()
        binding.unbind()
    }
}