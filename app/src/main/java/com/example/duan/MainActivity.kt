package com.example.duan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duan.adapter.adapteralbum
import com.example.duan.adapter.adaptersong
import com.example.duan.adapter.spacealbum
import com.example.duan.dbhelper.albumdbhelper
import com.example.duan.dbhelper.musicdbhelper
import com.example.duan.dbhelper.relationdbhelper
import com.example.duan.model.album_model
import com.example.duan.model.music_model
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mlistAlbum = mutableListOf<album_model>()//Danh sach cac album
    private var mlistSong = mutableListOf<music_model>()//Danh sach nhac co trong he thong
    lateinit var relation:relationdbhelper//lop nay chua cac phuong thuc de tuong tac với bảng relation
    lateinit var album:albumdbhelper//lớp này chứa các phương thức để tương tác với bảng album
    lateinit var music:musicdbhelper//lop nay chua cac phuong thuc de tuong tac voi bang music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        album=albumdbhelper(this)
        album.insertAlbum(album_model(8,"Album1",0))
        mlistAlbum=album.readAllAlbum()//Danh sach cac album co trong database
        home(topAppBar,this)
        album(mlistAlbum,rv_album,this)

        music= musicdbhelper(this)
        music.insertMusic(music_model(1,"http://...","KhongCamXuc","Ho Quang Hieu",12,0))
        music.insertMusic(music_model(1,"http://...","Vo Nguoi Ta","Phan Manh Quynh",12,0))
        mlistSong=music.readAllMusic()//Danh sach bai nhac co trong database
        song(mlistSong,rv_song,this)
    }
}

fun home(topAppBar: MaterialToolbar,context: Context) {
    topAppBar.setNavigationOnClickListener {
        // Handle navigation icon press
    }
    topAppBar.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.favorite -> {
                // Handle favorite icon press
                var intent:Intent = Intent(context, FavoriteActivity::class.java)
                startActivity(context,intent,intent.extras)
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

fun album(mlistAlbum:MutableList<album_model>,rv_album:RecyclerView,context: Context)
{
    var layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    rv_album.layoutManager = layoutManager
    //rv_album.itemAnimator=DefaultItemAnimator
    val adapter = adapteralbum(mlistAlbum)
    rv_album.adapter = adapter
    rv_album.addItemDecoration(spacealbum(1,2))}
fun song(mlistSong: MutableList<music_model>, rv_song:RecyclerView, context: Context)
{
    var layoutManager: LinearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    rv_song.layoutManager = layoutManager
    val adapter = adaptersong(mlistSong)
    rv_song.adapter = adapter
    rv_song.addItemDecoration(spacealbum(1,2))
}