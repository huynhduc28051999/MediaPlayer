@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.example.mediaplayer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaplayer.DBHandler.DatabaseHandlerMusic
import com.example.mediaplayer.adapter.adapteralbum
import com.example.mediaplayer.adapter.adaptersong
import com.example.mediaplayer.adapter.spacealbum
import com.example.mediaplayer.dbhelper.albumdbhelper
import com.example.mediaplayer.dbhelper.musicdbhelper
import com.example.mediaplayer.dbhelper.relationdbhelper
import com.example.mediaplayer.model.album_model
import com.example.mediaplayer.model.music_model
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.layout_home.*


class MainActivity : AppCompatActivity(), CreateAlbumDialog.DialogListener {
    private var mlistAlbum = mutableListOf<album_model>()//Danh sach cac album
    private var mlistSong = mutableListOf<music_model>()//Danh sach nhac co trong he thong
    lateinit var relation: relationdbhelper//lop nay chua cac phuong thuc de tuong tac với bảng relation
    lateinit var album: albumdbhelper//lớp này chứa các phương thức để tương tác với bảng album
    lateinit var music: musicdbhelper//lop nay chua cac phuong thuc de tuong tac voi bang music

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_home)
        music = musicdbhelper(this)
        album = albumdbhelper(this)
        checkUserPermission()
        mlistAlbum = album.readAllAlbum()//Danh sach cac album co trong database
        home(topAppBar, this)
        album(mlistAlbum, rv_album, this)

        btn_allSong.setOnClickListener {
            val intent: Intent = Intent(this, LoadAllSong::class.java)
            startActivity(this, intent, intent.extras)
        }
        btn_createAlbum.setOnClickListener {
            showNoticeDialog()
        }
    }

    fun showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        val dialog = CreateAlbumDialog()
        dialog.show(supportFragmentManager, "createAlbum")
    }

    private val REQUEST_CODE_ASK_PERMISSION = 123
    private fun checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSION
                )
                return
            }
            loadSong()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSION -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                loadSong()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun loadSong() {
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val cursor = contentResolver.query(allSongURI, null, selection, null, null)
        val list = ArrayList<SongInfo>()
        val db = DatabaseHandlerMusic(this)
        if (cursor !== null) {
            if (cursor.moveToFirst()) {
                do {
                    val songURL =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val time =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        } else {
                            TODO("VERSION.SDK_INT < Q")
                        }
                    val imageId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val song =
                        SongInfo(songName, songAuthor, songURL, time.toInt(), imageId.toLong())
                    db.addSong(song)
                    list.add(song)
                } while (cursor.moveToNext())
            }
            cursor.close()
            Service.setSongList(list)
            db.close()
        }
        mlistSong = music.readAllMusic()//Danh sach bai nhac co trong database
        song(mlistSong, rv_song, this)
    }

    override fun applyText(text: String) {
        val db = albumdbhelper(this)
        db.insertAlbum(text, false)
        mlistAlbum = album.readAllAlbum()//Danh sach cac album co trong database
        album(mlistAlbum, rv_album, this)
        db.close()
    }
}

fun home(topAppBar: MaterialToolbar, context: Context) {
    topAppBar.setNavigationOnClickListener {
        // Handle navigation icon press
    }
    topAppBar.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.favorite -> {
                val linearLayout = (context as Activity).findViewById<View>(R.id.linearAction)
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                if ((linearLayout as ViewGroup).childCount === 0) {
                    var view:View=inflater.inflate(R.layout.area_liked, linearLayout as LinearLayout);
                    var btn_album=view.findViewById(R.id.btn_album) as LinearLayout
                    btn_album.setOnClickListener {
                        var intent:Intent = Intent(context, AlbumLike::class.java)
                        startActivity(context,intent,intent.extras)
                    }
                    val btn_song_liked=view.findViewById(R.id.btn_song_liked) as LinearLayout
                    btn_song_liked.setOnClickListener {
                        // Handle favorite icon press
                        val intent = Intent(context, FavoriteActivity::class.java)
                        startActivity(context, intent, intent.extras)
                    }
                } else {
                    (linearLayout as ViewGroup).removeViewAt(0)
                    var view:View=inflater.inflate(R.layout.area_liked, linearLayout as LinearLayout);
                    var btn_album=view.findViewById(R.id.btn_album) as LinearLayout
                    btn_album.setOnClickListener {
                        var intent:Intent = Intent(context, AlbumLike::class.java)
                        startActivity(context,intent,intent.extras)
                    }
                    val btn_song_liked=view.findViewById(R.id.btn_song_liked) as LinearLayout
                    btn_song_liked.setOnClickListener {
                        // Handle favorite icon press
                        val intent = Intent(context, FavoriteActivity::class.java)
                        startActivity(context, intent, intent.extras)
                    }
                }
                true
                true
            }
            R.id.search -> {
                // Handle search icon press
                val linearLayout = (context as Activity).findViewById<View>(R.id.linearAction)
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                if ((linearLayout as ViewGroup).childCount === 0) {
                    inflater.inflate(R.layout.textview, linearLayout as LinearLayout)
                } else {
                    (linearLayout as ViewGroup).removeViewAt(0)
                    inflater.inflate(R.layout.textview, linearLayout as LinearLayout)
                }
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

fun album(mListAlbum: MutableList<album_model>, rv_album: RecyclerView, context: Context) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    rv_album.layoutManager = layoutManager
    //rv_album.itemAnimator=DefaultItemAnimator
    val adapter = adapteralbum(mListAlbum)
    rv_album.adapter = adapter
    rv_album.addItemDecoration(spacealbum(1, 2))
}

fun song(mListSong: MutableList<music_model>, rv_song: RecyclerView, context: Context) {
    val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    rv_song.layoutManager = layoutManager
    val adapter = adaptersong(mListSong, context)
    rv_song.adapter = adapter
    rv_song.addItemDecoration(spacealbum(1, 2))
}