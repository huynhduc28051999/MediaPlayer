package com.example.mediaplayer

import android.media.MediaPlayer

public class Service {
    companion object {
        var mp: MediaPlayer? = null
        var progress: Int = 0
        fun createMediaPlayer(mp: MediaPlayer) {
            this.mp = mp
        }
        fun setSbProgress(number: Int) {
            progress = number
        }
        fun seekToProgress(number: Int) {
            mp!!.seekTo(number)
        }
        fun stopPlay() {
            if (mp!!.isPlaying) {
                mp!!.stop()
            }
        }
        fun pausePlay() {
            if (mp!!.isPlaying) {
                mp!!.pause()
            }
        }
        fun resumePlay() {
            if (!mp!!.isPlaying) {
                mp!!.seekTo(progress)
                mp!!.start()
            }
        }
    }
}