package com.example.fitstream.presentation.detail_screen

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.example.fitstream.BuildConfig
import javax.inject.Inject


class ExoPlayerFacade @Inject constructor(
    private val context: Context
) {
    private lateinit var exoPlayer: ExoPlayer

    private var _isPlaying: MutableLiveData<Boolean> = MutableLiveData()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val playingListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            _isPlaying.value = isPlaying
        }
    }

    @OptIn(UnstableApi::class)
    fun initExoPlayer(videoLink: String, position: Long, playWhenReady: Boolean): ExoPlayer {
        if (this::exoPlayer.isInitialized) {
            return exoPlayer
        }

        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        exoPlayer = ExoPlayer
            .Builder(context)
            .setTrackSelector(trackSelector)
            .build().apply {
                val videoUri = BuildConfig.BASE_URL.plus(videoLink)
                val mediaItem = MediaItem.fromUri(videoUri)

                setMediaItem(mediaItem)
                prepare()

                seekTo(position)
                this.playWhenReady = playWhenReady
                addListener(playingListener)
            }

        return exoPlayer
    }

    fun releasePlayer() {
        if (this::exoPlayer.isInitialized) {
            exoPlayer.removeListener(playingListener)
            exoPlayer.release()
        }
    }

    fun playOrRestartIfEnd() {
        if (exoPlayer.playbackState == Player.STATE_ENDED) {
            exoPlayer.seekTo(0)
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            play()
        }
    }

    private fun play() {
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun rewind() {
        val currentPosition = exoPlayer.currentPosition
        val position = (currentPosition - 10000L).coerceAtLeast(0L)
        exoPlayer.seekTo(position)
    }

    fun forward() {
        val currentPosition = exoPlayer.currentPosition
        val duration = exoPlayer.duration
        val position = (currentPosition + 10000L).coerceAtMost(duration)
        exoPlayer.seekTo(position)
    }

    fun getPlaybackState(): Int {
        return exoPlayer.playbackState
    }

    fun getCurrentPlayBack(): CurrentPlayback {
        return CurrentPlayback(
            position = exoPlayer.currentPosition,
            playWhenReady = exoPlayer.playWhenReady
        )
    }
}

data class CurrentPlayback(
    val position: Long,
    val playWhenReady: Boolean
)