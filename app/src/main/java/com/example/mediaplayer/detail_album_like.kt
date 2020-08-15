package com.example.mediaplayer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.music_model
import com.example.mediaplayer.model.relation_model
import kotlinx.android.synthetic.main.activity_detail_album_like.*

class detail_album_like : AppCompatActivity() {
    lateinit var music: musicdbhelper//lop nay chua cac phuong thuc de tuong tac voi bang music
    lateinit var relation:relationdbhelper
    private var mlistSong = mutableListOf<music_model>()//danh sach nam trong album
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_album_like)
        var intent = intent
        var value = intent.getIntExtra("idAlbum", 0)
        var tenalbum=intent.getStringExtra("tenalbum")
        music = musicdbhelper(this)
        mlistSong = music.readAllMusicidAlbum(value)
        song(mlistSong, rv_music_like, this)
    }

    fun song(mlistSong: MutableList<music_model>, rv_music_like: RecyclerView, context: Context) {
        var layoutManager: LinearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_music_like.layoutManager = layoutManager
        val adapter = adaptersong(mlistSong, context)
        rv_music_like.adapter = adapter
        rv_music_like.addItemDecoration(spacealbum(1, 2))
    }
}