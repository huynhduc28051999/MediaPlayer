package com.example.mediaplayer

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player_processing.*
import java.util.concurrent.TimeUnit


class PlayerProcessing : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isPause: Boolean = false
        var isStop: Boolean = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_processing)
        val intent = intent
        val bundle = intent.extras
        if (bundle !== null){
            tvSongName.text = bundle.getString("mTitle")
            tvAuthor.text = bundle.getString("mAuthor")
            val size = bundle.getString("mSize")
            sbProgressPlayer.max = size!!.toInt()
            textviewNumber2.text = convertMilliseconds(size.toLong())
        }
        buttonStop.setOnClickListener {
            if (!isStop) {
                Service.stopPlay()
                isStop = true
                isPause = true
            }
        }
        buttonPause.setOnClickListener {
            if (!isStop) {
                if (!isPause) {
                    Service.pausePlay()
                    isPause = true
                } else {
                    Service.resumePlay()
                    isPause = false
                }
            } else {
                Service.startPlay(Service.currentPosition)
                isStop = false
            }
        }
        buttonNext.setOnClickListener {
            Service.nextSong()
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
        }
        buttonPre.setOnClickListener {
            Service.preSong()
            tvSongName.text = Service.listSongs[Service.currentPosition].mTitle
            tvAuthor.text = Service.listSongs[Service.currentPosition].mAuthorName
            sbProgressPlayer.max = Service.listSongs[Service.currentPosition].mSize
        }
        sbProgressPlayer?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
                Service.seekToProgress(seek.progress)
            }
        })
        var myTracking = MySongTrack()
        myTracking.start()
    }
    fun convertMilliseconds(milliseconds: Long): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = milliseconds / 1000 % 60
        return "${minutes}:${seconds}"
    }
    inner class MySongTrack(): Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    if (!Service.isStop) {
                        Service.setSbProgress(Service.mp.currentPosition)
                    }
                    val progress = Service.progress
                    sbProgressPlayer.progress = progress
                    textviewNumber1.text = convertMilliseconds(progress.toLong())
                }
            }
        }
    }
}