package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.adapter.adapteralbum
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.album_model
import com.example.mediaplayer.model.music_model
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity() {
    private var mlistAlbum = mutableListOf<album_model>()//Danh sach cac album
    private var mlistSong = mutableListOf<music_model>()//Danh sach nhac co trong he thong
    lateinit var relation:relationdbhelper//lop nay chua cac phuong thuc de tuong tac với bảng relation
    lateinit var album: albumdbhelper//lớp này chứa các phương thức để tương tác với bảng album
    lateinit var music: musicdbhelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        home(topAppBar,this)
        album=albumdbhelper(this)
        mlistAlbum=album.readAllAlbum()//Danh sach cac album co trong database
        album(mlistAlbum,rv_album,this)

        music= musicdbhelper(this)
        mlistSong=music.readAllMusic()//Danh sach bai nhac co trong database
        song(mlistSong,rv_song,this)
    }
    fun home(topAppBar: MaterialToolbar, context: Context) {
        topAppBar.setNavigationOnClickListener {
            // Handle navigation icon press
            var intent: Intent = Intent(context, MainActivity::class.java)
            ContextCompat.startActivity(context, intent, intent.extras)
        }
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // Handle favorite icon press
                    true
                }
                R.id.search -> {
                    // Handle search icon press
                    Log.i("a", "da nhan")
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }
    }
    fun album(mlistAlbum:MutableList<album_model>, rv_album: RecyclerView, context: Context)
    {
        var layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_album.layoutManager = layoutManager
        //rv_album.itemAnimator=DefaultItemAnimator
        val adapter = adapteralbum(mlistAlbum,context)
        rv_album.adapter = adapter
        rv_album.addItemDecoration(spacealbum(1,2))}
    fun song(mlistSong: MutableList<music_model>, rv_song: RecyclerView, context: Context)
    {
        var layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_song.layoutManager = layoutManager
        val adapter = adaptersong(mlistSong, context)
        rv_song.adapter = adapter
        rv_song.addItemDecoration(spacealbum(1,2))
    }
}