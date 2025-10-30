package com.stage.tv

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.stage.tv.databinding.ActivityPlayerBinding

import com.stage.tv.model.DetailResponse
import com.stage.tv.utils.Common
import com.stage.tv.utils.Common.Companion.isEllipsized
import com.stage.tv.viewmodel.DetailViewmodel
import com.stage.tv.viewmodel.DetailViewmodelFactory

class PlayerActivity : FragmentActivity() {

    lateinit var binding: ActivityPlayerBinding
    lateinit var viewmodel: DetailViewmodel
    val castFragment = ListFragment()

    private var player: ExoPlayer? = null

    private lateinit var playbackProgress: ProgressBar

    private lateinit var promo_url: String
    private val handler = Handler(Looper.getMainLooper())


    private val updateProgressAction = object : Runnable {
        override fun run() {
            val duration = player!!.duration.takeIf { it > 0 } ?: 0
            val position = player!!.currentPosition

            if (duration > 0) {
                val progressPercent = ((position.toFloat() / duration) * 100).toInt()
                playbackProgress.progress = progressPercent
            } else {
                playbackProgress.progress = 0
            }

            // Keep updating while playing
            if (player!!.isPlaying) {
                handler.postDelayed(this, 500)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializePlayer()
        setupControls()

        promo_url = intent.getStringExtra("promo_url").toString()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            /*val mediaItem = MediaItem.fromUri(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )*/
            val mediaItem = MediaItem.fromUri(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            )
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }

        binding.playerView.player = player
    }

    private fun setupControls() {
        // Access views from controller layout through playerView
        val controller = binding.playerView
        val rew = controller.findViewById<ImageButton>(R.id.exo_rew)
        val playPause = controller.findViewById<ImageButton>(R.id.exo_play_pause)
        val ffwd = controller.findViewById<ImageButton>(R.id.exo_ffwd)
        val skipTrailer = controller.findViewById<Button>(R.id.btnSkipTrailer)
        val timeText = controller.findViewById<TextView>(R.id.timeText)
        playbackProgress = controller.findViewById<ProgressBar>(R.id.progressBar)

        rew.setOnClickListener {
            val newPos = (player!!.currentPosition - 10_000).coerceAtLeast(0)
            player!!.seekTo(newPos)
            updateProgressBar()
        }

        ffwd.setOnClickListener {
            val newPos = (player!!.currentPosition + 10_000).coerceAtMost(player!!.duration)
            player!!.seekTo(newPos)
            updateProgressBar()
        }

        playPause.setOnClickListener {
            if (player!!.isPlaying) {
                player!!.pause()
                playPause.setImageResource(R.drawable.ic_exo_play)
            } else {
                player!!.play()
                playPause.setImageResource(R.drawable.ic_exo_play)
            }
        }

        skipTrailer.setOnClickListener {
            player!!.seekTo(player!!.duration)
        }

        player!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    val durationMs = player!!.duration // in milliseconds
                    val formatted = formatDur(durationMs)
                    Log.d("Player", "Total duration: $durationMs ms ($formatted)")
                    timeText.setText(formatted)
                }

                if (playbackState== Player.STATE_READY) {
                    handler.post(updateProgressAction)
                }
            }

            private fun formatDur(durationMs: Long): String {
                val totalSeconds = durationMs / 1000
                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60

                return if (hours > 0)
                    String.format("%02d:%02d:%02d", hours, minutes, seconds)
                else
                    String.format("%02d:%02d", minutes, seconds)
            }

        })
    }

    private fun updateProgressBar() {
        val duration = player?.duration?.takeIf { it > 0 } ?: return
        val position = player?.currentPosition ?: 0
        val progressPercent = ((position.toFloat() / duration) * 100).toInt()
        playbackProgress.progress = progressPercent
    }

    private fun releasePlayer() {
        handler.removeCallbacks(updateProgressAction)
        player?.release()
        player = null
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}