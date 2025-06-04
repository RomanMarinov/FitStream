package com.example.fitstream.presentation.detail_screen

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.example.fitstream.BuildConfig
import com.example.fitstream.R
import com.example.fitstream.databinding.ActivityDetailBinding
import com.example.fitstream.domain.model.VideoWorkout

class DetailActivity : AppCompatActivity() {

    private var playbackPosition: Long = 0L
    private var playWhenReady: Boolean = true
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var binding: ActivityDetailBinding

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        playbackPosition = exoPlayer.currentPosition
        playWhenReady = exoPlayer.playWhenReady
        outState.putLong("position", playbackPosition)
        outState.putBoolean("play", playWhenReady)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong("position", 0L)
            playWhenReady = savedInstanceState.getBoolean("play", true)
        }

        val workout = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("workout", VideoWorkout::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("workout")
        }

        initUI(workout = workout)
    }

    @OptIn(UnstableApi::class)
    private fun initUI(workout: VideoWorkout?) {
        val trackSelector = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }

        exoPlayer = ExoPlayer
            .Builder(this)
            .setTrackSelector(trackSelector)
            .build()
        Log.d("4444", " url my ulr=" + BuildConfig.BASE_URL.plus(workout?.link))
        val videoUri = BuildConfig.BASE_URL.plus(workout?.link)
        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare() // init
        exoPlayer.seekTo(playbackPosition)
        exoPlayer.playWhenReady = playWhenReady
        binding.player.player = exoPlayer
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }
}