package com.example.mediaplayer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.album_model
import com.example.mediaplayer.model.music_model
import kotlinx.android.synthetic.main.activity_detail_album_like.*
import kotlinx.android.synthetic.main.activity_detail_album_like.nav_bar_default

class Detail_album : AppCompatActivity() {
    lateinit var music: musicdbhelper//lop nay chua cac phuong thuc de tuong tac voi bang music
    lateinit var relation:relationdbhelper
    private lateinit var albumdbhelper: albumdbhelper
    private var album: album_model? = null
    private var mlistSong = mutableListOf<music_model>()//danh sach nam trong album
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_album_like)
        setSupportActionBar(nav_bar_default as androidx.appcompat.widget.Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var intent = intent
        var idAlbum = intent.getIntExtra("idAlbum", 0)
        var tenalbum=intent.getStringExtra("tenalbum")
        album_name.text = tenalbum
        music = musicdbhelper(this)
        mlistSong = music.readAllMusicidAlbum(idAlbum)
        albumdbhelper = albumdbhelper(this)
        album = albumdbhelper.getAlbum(idAlbum)
        if (album?.isLike == true) {
            btn_favorite.setImageResource(R.drawable.ic_favorite_24dp)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_favorite_border)
        }
        btn_favorite.setOnClickListener {
            albumdbhelper.likeOrNotLike(idAlbum)
            album = albumdbhelper.getAlbum(idAlbum)
            if (album?.isLike == true) {
                btn_favorite.setImageResource(R.drawable.ic_favorite_24dp)
            } else {
                btn_favorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }
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