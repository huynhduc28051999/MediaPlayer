package com.example.mediaplayer

import android.media.MediaPlayer

public class Service {
    companion object {
        var mp: MediaPlayer = MediaPlayer()
        var progress: Int = 0
        var listSongs = ArrayList<SongInfo>()
        var currentPosition = 0
        var isStop = true
        fun setSbProgress(number: Int) {
            progress = number
        }
        fun seekToProgress(number: Int) {
            mp.seekTo(number)
        }
        fun stopPlay() {
            mp.stop()
            mp.reset()
            this.isStop = true
        }
        fun pausePlay() {
            if (mp.isPlaying) {
                mp.pause()
            }
        }
        fun resumePlay() {
            if (!mp.isPlaying) {
                mp.seekTo(progress)
                mp.start()
            }
        }
        fun startPlay(currentPosition: Int) {
            if (this.currentPosition != currentPosition) {
                stopPlay()
                this.currentPosition = currentPosition
                mp.setDataSource(this.listSongs[currentPosition].mSongURL)
                mp.prepare()
                mp.start()
                this.isStop = false
            } else {
                if (isStop) {
                    mp.setDataSource(this.listSongs[currentPosition].mSongURL)
                    mp.prepare()
                    mp.start()
                    this.isStop = false
                }
                resumePlay()
            }
        }
        fun setSongList (list: ArrayList<SongInfo>) {
            this.listSongs = list
        }
    }
}