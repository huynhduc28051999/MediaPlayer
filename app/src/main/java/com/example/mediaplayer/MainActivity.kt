package com.example.mediaplayer

import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*
import java.io.FileDescriptor


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkUserPermission()

        var myTracking = MySongTrack()
        myTracking.start()
        sbProgress?.setOnSeekBarChangeListener(object :
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
    }

    inner class MySongAdapter(private val myDataset: ArrayList<SongInfo>) :
        RecyclerView.Adapter<MySongAdapter.MyViewHolder>() {

        inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            var tvSongName: TextView = view.tvSongName
            var tvAuthor: TextView = view.tvAuthor
            var button: Button = view.buttonPlay
            var image: ImageView = view.imageSong
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MySongAdapter.MyViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.song_ticket, parent, false) as View
            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val song = myDataset[position]
            holder.tvSongName.text = song.mTitle
            holder.tvAuthor.text =song.mAuthorName
            if (getAlbumart(song.mInageId) != null ){
                holder.image.setImageBitmap(getAlbumart(song.mInageId))
            }
            holder.button.setOnClickListener {
                try {
                    Service.startPlay(position)
                    sbProgress.max = Service.mp.duration
                    var intent = Intent(this@MainActivity, PlayerProcessing::class.java)
                    val bundle = Bundle()
                    bundle.putString("mTitle", song.mTitle)
                    bundle.putString("mAuthorName", song.mAuthorName)
                    bundle.putString("mSongURL", song.mSongURL)
                    bundle.putString("mSize", song.mSize.toString())
                    intent.putExtras(bundle)
                    startActivity(intent)
                } catch (ex: java.lang.Exception) {
                    Log.e("Player", ex.toString())
                }
            }
        }

        override fun getItemCount() = myDataset.size
    }
    inner class MySongTrack(): Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) {

                }
                runOnUiThread {
                    val isStop = Service.isStop
                    if (Service.isStop) {
                        sbProgress.max = 0
                        sbProgress.progress = 0
                        Service.setSbProgress(0)
                    } else {
                        val mp = Service.mp
                        sbProgress.max = mp.duration
                        sbProgress.progress = mp.currentPosition
                        Service.setSbProgress(mp.currentPosition)
                    }
                }
            }
        }
    }
    private val REQUEST_CODE_ASK_PERMISSION = 123
    private fun checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    !=  PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_ASK_PERMISSION)
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
    fun getAlbumart(album_id: Long?): Bitmap? {
        var bm: Bitmap? = null
        try {
            val sArtworkUri: Uri = Uri
                .parse("content://media/external/audio/albumart")
            val uri: Uri = ContentUris.withAppendedId(sArtworkUri, album_id!!)
            val pfd: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "r")
            if (pfd != null) {
                val fd: FileDescriptor = pfd.fileDescriptor
                bm = BitmapFactory.decodeFileDescriptor(fd)
            }
        } catch (e: java.lang.Exception) {
        }
        return bm
    }
    private fun loadSong() {
        val allSongURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC  + "!= 0"
        val cursor = contentResolver.query(allSongURI,null , selection, null, null)
        var list = ArrayList<SongInfo> ()
        if (cursor !== null) {
            if (cursor.moveToFirst()) {
                do {
                    val songURL = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val songAuthor = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val songName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val time = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val imageId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    list.add(SongInfo(songName, songAuthor, songURL, time.toInt(), imageId.toLong()))
                } while (cursor.moveToNext())
            }
            cursor.close()
            Service.setSongList(list)
            viewManager = LinearLayoutManager(this)
            viewAdapter = MySongAdapter(list)

            recyclerView = findViewById<RecyclerView>(R.id.listSongView).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter

            }
        }
    }
}